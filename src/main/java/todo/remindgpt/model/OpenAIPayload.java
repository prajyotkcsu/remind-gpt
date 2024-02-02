package todo.remindgpt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OpenAIPayload implements Serializable {
    private String model="gpt-3.5-turbo";
    private List<Messages> messages;
}


