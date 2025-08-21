package planner.project.spring_boot_starter_kafka.config.topics;

/**
 * @author 4ndr33w
 * @version 1.0
 */
public record KafkaTopic(
        String name,
        int partitions,
        short replicationFactor
) {
}
