package leonil.sulude.log.config;

import leonil.sulude.log.messaging.RabbitMQConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange logExchange() {
        return new TopicExchange(RabbitMQConstants.LOG_EXCHANGE);
    }

    @Bean
    public Queue logQueue() {
        return QueueBuilder.durable(RabbitMQConstants.LOG_QUEUE).build();
    }

    @Bean
    public Binding logBinding() {
        return BindingBuilder
                .bind(logQueue())
                .to(logExchange())
                .with(RabbitMQConstants.LOG_ROUTING_KEY);
    }
}
