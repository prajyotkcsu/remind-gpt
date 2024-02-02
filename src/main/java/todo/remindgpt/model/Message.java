package todo.remindgpt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String role;
    private String content;
}
