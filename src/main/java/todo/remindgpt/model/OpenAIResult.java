package todo.remindgpt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAIResult {
    private List<Choices> choices;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Choices{
        private Message message;
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        private  static class Message{
            private String content;
        }
    }
}
