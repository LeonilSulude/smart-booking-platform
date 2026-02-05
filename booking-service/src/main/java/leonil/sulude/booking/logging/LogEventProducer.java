package leonil.sulude.booking.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import leonil.sulude.booking.logging.dto.LogEventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class LogEventProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public void send(LogEventMessage event) {
        try {
            // Ensure timestamp exists
            if (event.getTimestamp() == null) {
                event.setTimestamp(Instant.now());
            }

            String payload = objectMapper.writeValueAsString(event);

            rabbitTemplate.convertAndSend(
                    RabbitMQConstants.LOG_QUEUE,
                    payload
            );

            log.debug(
                    "Log event sent | service={} | level={} | event={}",
                    event.getServiceName(),
                    event.getLevel(),
                    event.getEventType()
            );

        } catch (Exception e) {
            // Logging failures must never break business logic
            log.error("Failed to publish log event", e);
        }
    }
}

