package todo.remindgpt.service;

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
import todo.remindgpt.model.Tasks;

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

    public void produce(Tasks tasks) {
        log.info("Tasks from gpt:{} ", tasks);
        for (Task task : tasks.getTasks()) {
            // Specify a partition based on the task type (you can use your own logic)
            int partition;
            if (task.getTaskType().equals("fitness")) {
                partition = 0;
            } else if (task.getTaskType().equals("wellbeing")) {
                partition = 1;
            } else {
                partition = 2;
            }
            //int partition = Math.abs(task.getTaskType().hashCode()) % 3; //todo: make partition count dynamic

            log.info("task publishing to partition: {}", partition);
            producer.send(topic, partition, task.getTaskType(), String.valueOf(task));
        }
        log.info("{} tasks published on the topic {}", tasks.getTasks().size(), topic);
        log.info("*********produce end *********");
    }
    public void getLastProcessedTask(int duration) {
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
                }
            }
        }
        consumer.commitSync();
    }
}
