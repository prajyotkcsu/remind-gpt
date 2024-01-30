package todo.remindgpt.service;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.clients.admin.NewPartitions;
import org.apache.kafka.common.config.ConfigResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaTopicService {
    @Value("${spring.kafka.bootstrap.servers}")
    private String bootstrapServers;

    public void updateRetention(String topicName) {
        Properties adminProps = new Properties();
        adminProps.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        try (AdminClient adminClient = AdminClient.create(adminProps)) {
            ConfigResource resource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);

            Config retentionConfig = new Config(Collections.singletonList(
                    new ConfigEntry("retention.ms", Long.toString(1000))
            ));

            Map<ConfigResource, Config> configs = Collections.singletonMap(resource, retentionConfig);

            adminClient.alterConfigs(configs).all().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            // Handle exception as needed
        }
    }
}
