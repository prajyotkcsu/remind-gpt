package todo.remindgpt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import todo.remindgpt.repositories.Category;
import todo.remindgpt.repositories.CategoryCacheRepository;

@Service
public class RedisService {

    @Autowired
    private CategoryCacheRepository categoryCacheRepository;

    public void saveCategory(String key, int value) {
        Category category = new Category();
        category.setKey(key);
        category.setValue(value);
        categoryCacheRepository.save(category);
    }
    public int getLastPartition(){
        return categoryCacheRepository.findById("lastPartition").get().getValue();
    }
    public void updateLastPartition(){
        Category category=new Category();
        category.setKey("lastPartition");
        category.setValue(categoryCacheRepository.findById("lastPartition").get().getValue()+1);
        categoryCacheRepository.save(category);
    }
    public int getCategory(String key) {
        Category redisCache = categoryCacheRepository.findById(key).orElse(null);
        return (redisCache != null) ? redisCache.getValue() : 0;
    }
}
