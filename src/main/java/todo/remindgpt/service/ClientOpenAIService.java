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
        for(String cat: cats){
            CategoryRedisCache categoryRedisCache=new CategoryRedisCache();
            categoryRedisCache.setKey(cat);
            categoryRedisCache.setValue(0);
            categoryCacheRepository.save(categoryRedisCache);
        }
        log.info("Categories saved");
    }
    private String[] getCategories(){
        String[] cats={};
        Iterable<CategoryRedisCache> categories= categoryCacheRepository.findAll();
        categories.forEach(x-> System.out.println(x.getKey())); //lambda and collect to list
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
