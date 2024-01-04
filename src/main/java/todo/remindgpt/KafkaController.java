package todo.remindgpt;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.*;

@RestController
@Slf4j
public class KafkaController {

    @Value("${spring.kafka.properties.bootstrap.servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @GetMapping("/consume")
    public List<String> consumeKafkaMessages() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        //properties.put("group.id", groupId);
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // Get a list of available partitions for a specific topic
        String topic = "todo-assist";
        List<PartitionInfo> partitionInfoList = consumer.partitionsFor(topic);
        log.info("Number of partitions: {}", partitionInfoList.size());
        List<String> messages = new ArrayList<>();

        for (PartitionInfo partitionInfo : partitionInfoList) {
            int partition = partitionInfo.partition();

            // Assign the consumer to a specific partition
            consumer.assign(Collections.singletonList(new org.apache.kafka.common.TopicPartition(topic, partition)));

            // Seek to the beginning of the partition
            consumer.seekToBeginning(Collections.singletonList(new org.apache.kafka.common.TopicPartition(topic, partition)));

            // Consume messages from the partition
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, String> record : records) {
                    // Process the Kafka record
                    String message = record.value();
                    messages.add(message);
                }

                // Break the loop if no more messages are available
                if (records.isEmpty()) {
                    break;
                }
            }
        }

        // Close the consumer
        consumer.close();

        return messages;
    }
}
