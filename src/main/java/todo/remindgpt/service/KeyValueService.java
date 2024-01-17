package todo.remindgpt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import todo.remindgpt.repositories.KeyValue;
import todo.remindgpt.repositories.KeyValueRepository;

@Service
public class KeyValueService {

    @Autowired
    private KeyValueRepository keyValueRepository;

    public void saveKeyValue(String key, int value) {
        KeyValue keyValue = new KeyValue();
        keyValue.setKey(key);
        keyValue.setValue(value);
        keyValueRepository.save(keyValue);
    }
    public int getValueByKey(String key) {
        KeyValue keyValue = keyValueRepository.findById(key).orElse(null);
        return (keyValue != null) ? keyValue.getValue() : -1;
    }
}
