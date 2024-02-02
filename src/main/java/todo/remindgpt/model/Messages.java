package todo.remindgpt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public
class Messages implements Serializable {
    private String role="user";
    private String content="Categorize tasks 1)commit git code 2) plant trees 3) talk to a friend 4) water plants- each into just one of these categories self-dev, socialize, wellbeing, chores; give me output in this format [task: category]";
}