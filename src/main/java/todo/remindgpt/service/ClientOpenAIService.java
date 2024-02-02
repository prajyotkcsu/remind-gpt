package todo.remindgpt.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import todo.remindgpt.model.Message;
import todo.remindgpt.model.OpenAIPayload;
import todo.remindgpt.repositories.Category;
import todo.remindgpt.repositories.CategoryCacheRepository;
import todo.remindgpt.repositories.RedisRepository;

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
        OpenAIPayload openAIPayload=new OpenAIPayload();
        //todo: call openai api here and responde with map of task and type
        restTemplate.exchange("https://api.openai.com/v1/chat/completions", HttpMethod.POST,new HttpEntity<>(openAIPayload),OpenAIResult.class);
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
