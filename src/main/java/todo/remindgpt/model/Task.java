package todo.remindgpt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class Task implements Serializable{
    private String taskDesc;
    private int taskPriority;
    private long taskDuration;
}
