package planner.project.spring_boot_starter_kafka.config;

import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import planner.project.spring_boot_starter_kafka.config.consumer.Consumer;
import planner.project.spring_boot_starter_kafka.config.producer.Producer;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@ConfigurationProperties(prefix = "properties.kafka")
@Getter
@Builder
public class KafkaProperties {
    private String bootstrapServers;
    private String avroSchemaPath;
    private Producer producer;
    private Consumer consumer;
}