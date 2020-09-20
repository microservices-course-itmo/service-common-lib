package com.wine.to.up.commonlib.logging;

public enum CommonNotableEvents implements NotableEvent {
    W_KAFKA_SEND_MESSAGE_FAILED("Kafka send message failed. Topic: {}"),
    F_KAFKA_CONSUMER_POLL_FAILED("Kafka consumer polling failed. Topic: {}"),
    W_KAFKA_LISTENER_INTERRUPTED("Listener thread has been interrupted! Consuming topic name: {}"),
    W_EXECUTOR_SHUT_DOWN("Executor has been shut down. Name or id: {}");

    private final String template;

    CommonNotableEvents(String template) {
        this.template = template;
    }

    @Override
    public String getTemplate() {
        return template;
    }

    @Override
    public String getName() {
        return name();
    }
}
