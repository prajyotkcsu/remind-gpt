package todo.remindgpt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todo.remindgpt.model.TaskInputPayload;
import todo.remindgpt.service.ClientOpenAIService;

@RestController
@RequestMapping("/reminder/api/todo")
@Slf4j
public class ClientOpenAIController {
    @Autowired
    ClientOpenAIService clientOpenAIService;

    @PostMapping("cat")
    String postCategories(@RequestBody String cat) {
        //parse and save to redis.
        log.info("Declaring todo categories......{}", cat);
        String[] cats = cat.split(";");
        clientOpenAIService.initCategories(cats);
        return "";
    }

    @PostMapping("task")
    String postTasks(@RequestBody String task) {
        log.info("submitting tasks......{}", task);
        clientOpenAIService.initPrompt(task);
        return "";
    }

    @PostMapping("")
    String postInput(@RequestBody TaskInputPayload taskPayload){
        clientOpenAIService.parseInput(taskPayload);
        return "";
    }


}
