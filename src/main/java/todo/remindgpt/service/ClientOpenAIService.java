package todo.remindgpt.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import todo.remindgpt.model.*;
import todo.remindgpt.repositories.Category;
import todo.remindgpt.repositories.CategoryCacheRepository;

import java.util.*;

@Service
@Slf4j
public class ClientOpenAIService {
    @Autowired
    private CategoryCacheRepository categoryCacheRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private RedisService redisService;

    @Value("${openai.content}")
    private String content;
    @Value("${openai.secret}")
    private String secret;
    @Value("${openai.url}")
    private String url;

    public ClientOpenAIService() {
    }

    public int initCategories(@NonNull CategoriesInputPayload cats){
        String[] categories=cats.getCategories().toArray(new String[0]);
        int partition=0;
        categoryCacheRepository.deleteAll();
        for(String cat: categories){
            Category category=new Category();
            category.setKey("cat");
            category.setValue(partition++);
            categoryCacheRepository.save(category);
        }
        log.info("{} new categories saved", categories.length);
        return categories.length;
    }

    public String initPrompt(TaskInputPayload tasks) {
        String prompt = generatePrompt(tasks);
        log.info("prompt sending to openai:{}", prompt);
        HttpEntity<OpenAIPayload> requestEntity = openAPIRequestBuilder(prompt);
        boolean retry = false;
        while (!retry) {
            //todo: call openai api here and responde with map of task and type
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            Map<String, String> taskType = getTaskAndType(responseEntity.getBody().toString());

        }
        return "";
    }

    Map<String,String> getTaskAndType(String responseJSON){
        Map<String,String> hm=new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        String out = "";
        try {
            JsonNode jsonNode = objectMapper.readTree(responseJSON);
            out = jsonNode.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception ex) {

        }
        String[] taskList = out.split("\n");
        List<String> producedCategories = new ArrayList<>();
        for (String task : taskList) {
            String[] pair = task.split(":");
            hm.put(pair[1].trim().toLowerCase(),pair[0].trim().toLowerCase());
        }

//        if (!catMap.containsKey(key)) {
//            log.info("category returned by openAI is not declared by the user");
//            continue;
//        } else {
//            kafkaService.produce(catMap.get(key), key, value);
//            producedCategories.add(key + " " + value);
//        }
        return hm;
    }
    String generatePrompt(TaskInputPayload tasks){
        List<String> taskList=new ArrayList<>();
        for(int i=0;i<tasks.getTasks().size();i++){
            taskList.add(tasks.getTasks().get(i).getTaskDesc());
        }
        String[] taskArray=taskList.toArray(new String[0]);
        String prompt;
        Iterable<Category> cats=categoryCacheRepository.findAll();
        Map<String,Integer> catMap = new HashMap<>();
        for (Category cat : cats) {
            catMap.put(cat.getKey(),cat.getValue());
        }
        List<String> catKeys=new ArrayList<>();
        for(String cat: catMap.keySet()){
            catKeys.add(cat);
        }
        String[] categoriesArray=catKeys.toArray(new String[0]);

        content=content.replace("[tasks]",String.join(",",taskArray));
        prompt=content.replace("[categories]",String.join(",",categoriesArray));
        return prompt;
    }
    HttpEntity<OpenAIPayload> openAPIRequestBuilder(String prompt){
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setBearerAuth(secret);
        OpenAIPayload openAIPayload = new OpenAIPayload();
        openAIPayload.setModel("gpt-3.5-turbo");
        Messages message = new Messages();
        message.setRole("user");
        message.setContent(prompt);
        List<Messages> messagesList = Arrays.asList(message);
        openAIPayload.setMessages(messagesList);
        HttpEntity<OpenAIPayload> requestEntity = new HttpEntity<>(openAIPayload, httpHeaders);
        return requestEntity;
    }
}
