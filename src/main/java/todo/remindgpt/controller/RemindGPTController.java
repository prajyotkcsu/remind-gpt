package todo.remindgpt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import todo.remindgpt.model.CategoriesInputPayload;
import todo.remindgpt.model.TaskInputPayload;
import todo.remindgpt.service.ClientOpenAIService;

@RestController
@RequestMapping("/reminder/api/")
@Slf4j
public class RemindGPTController {
    @Autowired
    ClientOpenAIService clientOpenAIService;

    @PostMapping("cat")
    String postCategories(@RequestBody CategoriesInputPayload cat) {
        int categoryCount=clientOpenAIService.initCategories(cat);
        return categoryCount+" new categories defined by the user";
    }
    @PostMapping("task")
    String postTasks(@RequestBody TaskInputPayload tasks) {
        log.info("submitting tasks......{}", tasks);
        String response=clientOpenAIService.initPrompt(tasks);
        return response;
    }
}
