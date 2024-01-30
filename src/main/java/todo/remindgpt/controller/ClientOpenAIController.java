package todo.remindgpt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todo.remindgpt.service.ClientOpenAIService;

@RestController
@RequestMapping("/reminder/api/todo")
@Slf4j
public class ClientOpenAIController {
    @Autowired
    ClientOpenAIService clientOpenAIService;
    @PostMapping("cat")
    String initCategories(@RequestBody String cat){
        log.info("Declaring todo categories......{}",cat);
        String[] cats=cat.split(";");
        clientOpenAIService.initCategories(cats);
        return "";
    }
    @PostMapping("text")
    String sendTextToOpenAI(@RequestBody String todo){
        log.info("Sending text to OpenAI API......{}",todo);
        clientOpenAIService.parseInput(todo);
        return "";
    }
}
