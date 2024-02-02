package todo.remindgpt.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import todo.remindgpt.model.Message;
import todo.remindgpt.model.Messages;
import todo.remindgpt.model.OpenAIPayload;
import todo.remindgpt.model.OpenAIResult;
import todo.remindgpt.repositories.Category;
import todo.remindgpt.repositories.CategoryCacheRepository;
import todo.remindgpt.repositories.RedisRepository;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ClientOpenAIService {
    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private CategoryCacheRepository categoryCacheRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${openai.content}")
    private String content;

    public ClientOpenAIService() {
    }

    public void initCategories(String[] cats){
        int partition=0;
        for(String cat: cats){
            Category category=new Category();
            category.setId(cat);
            category.setPartition(partition++);
            categoryCacheRepository.save(category);
        }
        log.info("{} new categories saved", cats.length);
    }

    public String initPrompt(String tasks){
        String prompt;
        Iterable<Category> cats=categoryCacheRepository.findAll();
        StringBuffer categories=new StringBuffer();
        for(Category cat: cats){
            categories.append(cat.getId());
            categories.append(", ");
        }
        content=content.replace("[tasks]",tasks);
        prompt=content.replace("[categories]",categories.toString());
        log.info("prompt sending to openai:{}", prompt);
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setBearerAuth("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");
        OpenAIPayload openAIPayload = new OpenAIPayload();
        openAIPayload.setModel("gpt-3.5-turbo");

        Messages message = new Messages();
        message.setRole("user");
        message.setContent("Categorize tasks 1)commit git code 2) plant trees 3) talk to a friend 4) water plants- each into just one of these categories self-dev, socialize, wellbeing, chores; give me output in this format [task: category]");

        List<Messages> messagesList = Arrays.asList(message);
        openAIPayload.setMessages(messagesList);
        HttpEntity<OpenAIPayload> requestEntity = new HttpEntity<>(openAIPayload, httpHeaders);

        //todo: call openai api here and responde with map of task and type
        ResponseEntity<OpenAIResult> responseEntity=restTemplate.exchange("https://api.openai.com/v1/chat/completions", HttpMethod.POST,requestEntity, OpenAIResult.class);
        log.info("response:{}",responseEntity.getBody().getChoices().get(0));
        return prompt;
    }
    public String[] getCategories(){
        String[] cats=new String[(int)categoryCacheRepository.count()];
        Iterable<Category> categories= categoryCacheRepository.findAll();
        int i=0;
        for(Category c: categories){
            cats[i++]=c.getId();
        }
        return cats;
    }

    public void parseInput(String inputText){
        String[] redisCats;
        String[] inputs=inputText.split(";");
        Message messages=new Message();
        getCategories();
        content.replace("[tasks]",inputs.toString());
       // content.replace("[categories]",redisCats.toString());
        System.out.println("message"+content);
    }
}
