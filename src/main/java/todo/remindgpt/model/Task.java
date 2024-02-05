package todo.remindgpt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task implements Serializable{
    private String taskDesc;
    private int taskPriority;
    private long taskDuration;
}

/*
{
"tasks" : [{"task_type":"fitness",
"task_desc":"life weigths",
"task_priority": "8",
"task_duration": "75"
},{"task_type":"study",
"task_desc":"learn elasticsearch concept",
"task_priority": "6",
"task_duration": "30"
},{"task_type":"wellbeing",
"task_desc":"take a walk",
"task_priority": "6"
},{"task_type":"work",
"task_desc":"commit code on git",
"task_priority": "8"
}],
"timestamp": 2323113
}



 */