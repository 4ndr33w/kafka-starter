package planner.project.spring_boot_starter_kafka.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author 4ndr33w
 * @version 1.0
 */
@Component
public class KafkaMessageConverter {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public KafkaMessageConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> Message<byte[]> toKafkaMessage(String topic, String key, T object) {
        try {
            byte[] payload = objectMapper.writeValueAsBytes(object);

            return MessageBuilder.withPayload(payload)
                    .setHeader(KafkaHeaders.TOPIC, topic)
                    .setHeader(KafkaHeaders.KEY, key)
                    .setHeader("X-Object-Type", object.getClass().getSimpleName())
                    .build();

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert object to Kafka message", e);
        }
    }

    public <T> T fromKafkaMessage(Message<?> message, Class<T> clazz) {
        try {
            String jsonStr = extractJsonFromPayload(message.getPayload());
            return objectMapper.readValue(jsonStr, clazz);

        } catch (Exception e) {
            throw new RuntimeException("Failed to convert Kafka message to object of type " + clazz.getSimpleName(), e);
        }
    }

private boolean isBase64(String str) {
    if (str == null || str.trim().isEmpty()) {
        return false;
    }
    try {
        Base64.getDecoder().decode(str.trim());
        return true;
    } catch (IllegalArgumentException e) {
        return false;
    }
}

    private String extractJsonFromPayload(Object payload) {
        String payloadStr;

        if (payload instanceof byte[]) {
            payloadStr = new String((byte[]) payload, StandardCharsets.UTF_8);
        } else if (payload instanceof String) {
            payloadStr = (String) payload;
        } else {
            throw new IllegalArgumentException("Unsupported payload type: " + payload.getClass());
        }

        try {
            if (isBase64(payloadStr)) {
                byte[] decodedBytes = Base64.getDecoder().decode(payloadStr);
                return new String(decodedBytes, StandardCharsets.UTF_8);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("can't decode from base64: " + payload.getClass());
        }
        return payloadStr;
    }
}
