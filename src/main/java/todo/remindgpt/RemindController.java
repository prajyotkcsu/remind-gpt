package todo.remindgpt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RemindController {

    @Autowired
    private KafkaProducer kafkaProducer;

    public RemindController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/tasks")
    public void sendMessage(@RequestBody Task message) {
        kafkaProducer.produceJsonMessage(message);
    }
}
