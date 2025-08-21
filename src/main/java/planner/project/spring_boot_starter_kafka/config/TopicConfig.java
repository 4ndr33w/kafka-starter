package planner.project.spring_boot_starter_kafka.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Map;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaProperties.class)
@ConditionalOnProperty(prefix = "properties.kafka", name = "min.insync.replicas")
public class TopicConfig {

    @Value("${properties.kafka.min.insync.replicas}")
    private String minInsyncReplicas;

    @Bean
    public NewTopic createTopic(String name, int partitions, short replicas) {

        return TopicBuilder
                .name(name)
                .partitions(partitions)
                .replicas(replicas)
                .configs(Map.of("min.insync.replicas", minInsyncReplicas))
                .build();
    }
}