package todo.remindgpt.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import todo.remindgpt.model.Task;
import todo.remindgpt.model.Tasks;

import java.time.Duration;
import java.util.*;

@Service
@Slf4j
public class KafkaService {

    @Autowired
    private KafkaTemplate<String, String> kafkaProducerTemplate;
    @Autowired
    private KafkaConsumer<String, String> kafkaConsumerTemplate;
    private final String topic = "todo-assist";
    public void produce(Tasks tasks) {
        log.info("Tasks from gpt:{} ",tasks);
        for(Task task: tasks.getTasks()){
            // Specify a partition based on the task type (you can use your own logic)
            int partition = Math.abs(task.getTaskType().hashCode()) % 5;

            log.info("task publishing to partition: {}",task.getTaskType());
            kafkaProducerTemplate.send(topic,task.getTaskType(), String.valueOf(task));
        }
        log.info("{} tasks published on the topic {}",tasks.getTasks().size(), topic);
        log.info("*********produce end *********");
    }

    /***
     * returns list of tasks from each partition. maintain offset
     * @param duration
     * @return
     */
    public Map<String, String> consume(int duration) {
        log.info("*********consume*********");
        List<PartitionInfo> partitionInfoList = kafkaConsumerTemplate.partitionsFor(topic);
        log.info("partitionInfoList{}",partitionInfoList);
        log.info("Number of partitions: {}", partitionInfoList.size());
        Map<String, String> messages = new HashMap<>();
        for (PartitionInfo partitionInfo : partitionInfoList) {
            int partition = partitionInfo.partition();
            log.info("partition number:{}",partition);
            // Assign the consumer to a specific partition
            kafkaConsumerTemplate.assign(Collections.singletonList(new TopicPartition(topic, partition)));

            long currentOffset = kafkaConsumerTemplate.position(new TopicPartition(topic, partition));
            log.info("partition:{} and offset:{} ",partition,currentOffset);
            kafkaConsumerTemplate.seek(new TopicPartition(topic, partition), currentOffset);
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumerTemplate.poll(Duration.ofMillis(100));
                if (records.isEmpty()) {
                    break;
                }
                for (ConsumerRecord<String, String> record : records) {
                    messages.put(record.key(), record.value());
                    log.info("Individual messages: {}",messages);
                }
            }
        }
        log.info("message: {}",messages);
        //kafkaConsumerTemplate.close();
        return messages;
    }
}
