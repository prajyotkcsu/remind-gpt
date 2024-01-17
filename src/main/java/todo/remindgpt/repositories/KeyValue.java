package todo.remindgpt.repositories;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("KeyValue")
public class KeyValue {
    @Id
    private String key;
    private int value;
    private int next;

    // Getters and setters
}
