package todo.remindgpt.repositories;

import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

@EnableRedisRepositories
public interface RedisRepository extends CrudRepository<RedisCache, String> {
}
