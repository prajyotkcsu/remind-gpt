package todo.remindgpt.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import todo.remindgpt.model.Messages;
import todo.remindgpt.repositories.CategoryCacheRepository;
import todo.remindgpt.repositories.CategoryRedisCache;
import todo.remindgpt.repositories.RedisCache;
import todo.remindgpt.repositories.RedisRepository;

@Service
@Slf4j
public class ClientOpenAIService {
    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private CategoryCacheRepository categoryCacheRepository;

    @Value("${openai.content}")
    private String content;

    public void initCategories(String[] cats){
        int partition=0;
        for(String cat: cats){
            CategoryRedisCache categoryRedisCache=new CategoryRedisCache();
            categoryRedisCache.setKey(cat);
            categoryRedisCache.setValue(partition++);
            categoryCacheRepository.save(categoryRedisCache);
        }
        log.info("{} new categories saved", cats.length);
    }

    public String initPrompt(String[] tasks,String[] cats){
        String prompt;
        content=content.replace("[tasks]",tasks.toString());
        prompt=content.replace("[cats]",cats.toString());
        //todo: call openai api here and responde with map of task and type
        return prompt;
    }
    public String[] getCategories(){
        String[] cats=new String[(int)categoryCacheRepository.count()];
        Iterable<CategoryRedisCache> categories= categoryCacheRepository.findAll();
        int i=0;
        for(CategoryRedisCache c: categories){
            cats[i++]=c.getKey();
        }
        return cats;
    }

    public void parseInput(String inputText){
        String[] redisCats;
        String[] inputs=inputText.split(";");
        Messages messages=new Messages();
        getCategories();
        content.replace("[tasks]",inputs.toString());
       // content.replace("[categories]",redisCats.toString());
        System.out.println("message"+content);
    }
}
