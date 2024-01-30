package todo.remindgpt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import todo.remindgpt.repositories.RedisRepository;
import todo.remindgpt.repositories.RedisCache;

@Service
public class RedisService {

    @Autowired
    private RedisRepository redisRepository;

    public void saveRedis(String key, int value) {
        RedisCache redisCache = new RedisCache();
        redisCache.setKey(key);
        redisCache.setValue(value);
        redisRepository.save(redisCache);
    }
    public int getRedis(String key) {
        RedisCache redisCache = redisRepository.findById(key).orElse(null);
        return (redisCache != null) ? redisCache.getValue() : -1;
    }
}
