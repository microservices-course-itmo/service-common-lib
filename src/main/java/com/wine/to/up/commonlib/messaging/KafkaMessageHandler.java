package com.wine.to.up.commonlib.messaging;

/**
 * Base interface for all classes responsible for handing messages of some specific type
 * @param <Message> the type of the messages being handled
 */
public interface KafkaMessageHandler<Message> {

    void handle(Message message);
}
