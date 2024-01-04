package todo.remindgpt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final String topicName = "todo-assist";

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void produceJsonMessage(Task message) {
        log.info("Message produced:{} ",message);
        kafkaTemplate.send(topicName,message.getType(), String.valueOf(message));
    }
}
