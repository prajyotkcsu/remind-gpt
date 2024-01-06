package todo.remindgpt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import todo.remindgpt.service.KafkaService;
import todo.remindgpt.model.Tasks;

@RestController
@Slf4j
public class RemindGPTController {
    @Autowired
    private KafkaService kafkaService;
    public RemindGPTController(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }
    @PostMapping("/produce")
    public void produce(@RequestBody Tasks message) {
        log.info("New task list arrived: {}",message);
        kafkaService.produce(message);
    }
    @GetMapping("/consume")
    public void consume() {
        kafkaService.consume(120);
    }
}

