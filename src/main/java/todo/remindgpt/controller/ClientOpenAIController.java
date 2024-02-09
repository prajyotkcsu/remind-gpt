package todo.remindgpt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import todo.remindgpt.model.CategoriesInputPayload;
import todo.remindgpt.model.TaskInputPayload;
import todo.remindgpt.service.ClientOpenAIService;

@RestController
@RequestMapping("/reminder/api/")
@Slf4j
public class ClientOpenAIController {
    @Autowired
    ClientOpenAIService clientOpenAIService;

    @PostMapping("cat")
    String postCategories(@RequestBody CategoriesInputPayload cat) {
        clientOpenAIService.initCategories(cat);
        return "";
    }

    @PostMapping("task")
    String postTasks(@RequestBody TaskInputPayload tasks) {
        log.info("submitting tasks......{}", tasks);
        clientOpenAIService.initPrompt(tasks);
        return "";
    }

//    @PostMapping("")
//    String postInput(@RequestBody TaskInputPayload taskPayload){
//        clientOpenAIService.parseInput(taskPayload);
//        return "";
//    }


}
