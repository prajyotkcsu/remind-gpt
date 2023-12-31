//package todo.remindgpt;
//
//import org.apache.kafka.common.serialization.Serializer;
//
//import java.util.Map;
//
//public class ReminderObjectSerializer implements Serializer<ReminderObject> {
//
//    @Override
//    public void configure(Map<String, ?> configs, boolean isKey) {
//        // No configuration needed
//    }
//
//    @Override
//    public byte[] serialize(String topic, ReminderObject data) {
//        if (data == null) {
//            return null;
//        }
//
//        // Implement your serialization logic here
//        // Example: convert ReminderObject to a byte array
//        // For simplicity, let's assume the ReminderObject has a toString method.
//        return data.toString().getBytes();
//    }
//
//    @Override
//    public void close() {
//        // No resources to release
//    }
//}
