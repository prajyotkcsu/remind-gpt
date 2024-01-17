package todo.remindgpt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import todo.remindgpt.model.Task;
import todo.remindgpt.model.TaskDTO;

import java.time.Duration;
import java.util.*;

@Service
@Slf4j
public class KafkaService {

    @Autowired
    private KafkaTemplate<String, String> producer;
    @Autowired
    private KafkaConsumer<String, String> consumer;
    private final String topic = "todo-assist";
    private static final int NUM_PARTITIONS = 3; // Adjust as needed
    private static int partitionCounter = 0;

    @Autowired
    private KeyValueService keyValueService;

    public static int assignPartition(String taskType) {
        int hashCode = taskType.hashCode();
        int partition = Math.abs(hashCode % NUM_PARTITIONS);
        return partition;
    }

    public List<String> produce(TaskDTO tasks) {
        log.info("Tasks from gpt:{} ", tasks);


        List<String> producedTasks=new ArrayList<>();
        int i=0;
        for (Task task : tasks.getTasks()) {
            int partition=keyValueService.getValueByKey(task.getTaskType());
            if(partition<0){
                keyValueService.saveKeyValue(task.getTaskType(),i);
                partition=i;
            }
            producedTasks.add(String.format("task: %s produced to partition:%s",task,partition));
            log.info("task publishing to partition: {}", partition);
            producer.send(topic, partition, task.getTaskType(), task.toString());
            i++;
        }
        log.info("{} tasks published on the topic {}", tasks.getTasks().size(), topic);
        log.info("*********produce end *********");
    return producedTasks;}
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
                    Task t = new Task(taskType, taskDesc, taskPriority, taskDuration);
                    orderedTasks.offer(t);
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
}
