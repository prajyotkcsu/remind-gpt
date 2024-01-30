package todo.remindgpt.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import todo.remindgpt.model.Messages;
import todo.remindgpt.repositories.RedisCache;
import todo.remindgpt.repositories.RedisRepository;

@Service
@Slf4j
public class ClientOpenAIService {
    @Autowired
    private RedisRepository redisRepository;

    @Value("${openai.content}")
    private String content;

    public void initCategories(String[] cats){
        for(String cat: cats){
            RedisCache redisCache=new RedisCache();
            redisCache.setKey(cat);
            redisCache.setValue(0);
            redisRepository.save(redisCache);
        }
        log.info("Categories saved");
    }

    public void parseInput(String inputText){
        String[] redisCats;

        String[] inputs=inputText.split(";");
        Messages messages=new Messages();
        content.replace("[tasks]",inputs.toString());
        content.replace("[categories]",redisCats.toString());
        System.out.println("message"+content);
    }
}
