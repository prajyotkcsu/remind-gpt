package todo.remindgpt.repositories;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("CategoryCache")
public class CategoryRedisCache {
    @Id
    private String key;
    private int value;
    private int next;
}
