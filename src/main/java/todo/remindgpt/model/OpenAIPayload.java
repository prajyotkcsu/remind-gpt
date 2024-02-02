package todo.remindgpt.model;

import java.util.List;

public class OpenAIPayload {
    private String model="gpt-3.5-turbo";
    private List<Messages> messages;

    public static class Messages{
    private String role="user";
    private String content="Categorize tasks 1)commit git code 2) plant trees 3) talk to a friend 4) water plants- each into just one of these categories self-dev, socialize, wellbeing, chores; give me output in this format [task: category]";
    }

}

