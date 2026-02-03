package leonil.sulude.log.messaging;

public final class RabbitMQConstants {

    private RabbitMQConstants() {}

    public static final String LOG_EXCHANGE = "app.logs.exchange";
    public static final String LOG_QUEUE = "app.logs.queue";
    public static final String LOG_ROUTING_KEY = "app.logs.#";
}
