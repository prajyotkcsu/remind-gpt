package todo.remindgpt.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.ConfigResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import todo.remindgpt.model.Task;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class KafkaService {

    @Autowired
    private KafkaTemplate<String, String> producer;
    @Autowired
    private KafkaConsumer<String, String> consumer;
    private final String topic = "todo-assist";

    public boolean produce(int partition,String key, String value) {
        log.info("message details key: {}, value: {}, partition: {}, topic: {}", key,value,partition,topic);
        producer.send(topic,partition,key,value);
        log.info("*********produce end *********");
        return true;
    }

    public List<Task> getLastProcessedTask(int duration) throws Exception {
        Queue<Task> orderedTasks=new PriorityQueue<>((o1, o2) -> Integer.compare(o2.getTaskPriority(),o1.getTaskPriority()));

        List<PartitionInfo> partitionInfoList = consumer.partitionsFor(topic);
        List<TopicPartition> partitionsToAssign = new ArrayList<>();
        for (PartitionInfo partitionInfo : partitionInfoList) {
            partitionsToAssign.add(new TopicPartition(topic, partitionInfo.partition()));
        }
        consumer.assign(partitionsToAssign);
        for (TopicPartition partition : partitionsToAssign) {
            consumer.seekToEnd(Collections.singleton(partition));
            long lastProcessedOffset=consumer.position(partition);
            log.info("last position of {} is {}",partition,lastProcessedOffset);
            consumer.seek(partition,lastProcessedOffset-1);
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            if (!records.isEmpty()) {
                for (ConsumerRecord<String, String> record : records) {
                    log.info("Partition: {}, Offset: {}, Key: {}, Value: {}",
                            record.partition(), record.offset(), record.key(), record.value());
                    String stringValue = record.value().toString();
                    // Extract values from the string (you may need to adjust this based on the actual format)
                    String taskType = extractValue(stringValue, "taskType");
                    String taskDesc = extractValue(stringValue, "taskDesc");
                    int taskPriority = Integer.parseInt(extractValue(stringValue, "taskPriority"));
                    int taskDuration = Integer.parseInt(extractValue(stringValue, "taskDuration"));
                    // Create a Task object using the extracted values
                    //Task t = new Task(taskType, taskDesc, taskPriority, taskDuration);
                   // orderedTasks.offer(t);
                }
            }
        }

        consumer.commitSync();
        log.info("orderedTasks: {}",orderedTasks);
    return new LinkedList<>(orderedTasks);
    }
    private static String extractValue(String input, String key) {
        String keyPrefix = key + "=";
        int startIndex = input.indexOf(keyPrefix);
        if (startIndex != -1) {
            startIndex += keyPrefix.length();
            int endIndex = input.indexOf(",", startIndex);
            if (endIndex == -1) {
                endIndex = input.indexOf(")", startIndex);
            }
            if (endIndex != -1) {
                return input.substring(startIndex, endIndex).trim();
            }
        }
        return "";
    }


    @Value("${spring.kafka.bootstrap.servers}")
    private String bootstrapServers;

    public void updateRetention() {
        Properties adminProps = new Properties();
        adminProps.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        try (AdminClient adminClient = AdminClient.create(adminProps)) {
            ConfigResource resource = new ConfigResource(ConfigResource.Type.TOPIC, topic);

            Config retentionConfig = new Config(Collections.singletonList(
                    new ConfigEntry("retention.ms", Long.toString(1000))
            ));

            Map<ConfigResource, Config> configs = Collections.singletonMap(resource, retentionConfig);

            adminClient.alterConfigs(configs).all().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            // Handle exception as needed
        }
    }
}
