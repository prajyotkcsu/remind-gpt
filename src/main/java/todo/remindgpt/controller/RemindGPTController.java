package todo.remindgpt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import todo.remindgpt.model.Task;
import todo.remindgpt.model.TaskDTO;
import todo.remindgpt.service.KafkaService;


import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class RemindGPTController {
    @Autowired
    private KafkaService kafkaService;
    public RemindGPTController(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }
    @PostMapping("/produce")
    public ResponseEntity<List<String>> produceToTopic(@RequestBody TaskDTO message) {
        log.info("New task list arrived: {}",message);
        List<String> producedTasks=kafkaService.produce(message);
        return ResponseEntity.ok(producedTasks);}
    @GetMapping("/consume")
    public ResponseEntity<TaskDTO> consumeFromTopic() {
        List<Task> orderedTasks = new ArrayList<>();
        TaskDTO taskDTO = null;
        try {
            orderedTasks = kafkaService.getLastProcessedTask(120);
            taskDTO = TaskDTO.builder()
                    .tasks(orderedTasks)
                    .build();
        } catch (Exception exp) {
            log.error(exp.getMessage());
        }
        return ResponseEntity.ok(taskDTO);
    }
}

