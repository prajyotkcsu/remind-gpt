package todo.remindgpt.repositories;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
@Data
@RedisHash("category")
public class Category{
    @Id
    private String key;
    private int value;
}
