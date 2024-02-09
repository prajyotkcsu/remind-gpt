package todo.remindgpt.model;

import lombok.Data;

import java.util.List;
@Data
public class CategoriesInputPayload {
    private List<String> categories;
}
