package com.wine.to.up.commonlib.messaging;

import com.wine.to.up.commonlib.annotations.InjectEventLogger;
import com.wine.to.up.commonlib.metrics.CommonMetricsCollector;
import com.wine.to.up.commonlib.logging.EventLogger;
import com.wine.to.up.commonlib.logging.CommonNotableEvents;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * Sends single (or maybe butch in future) message of given type in one given topic.
 *
 * Usage:
 *  If you want to send messages to some certain topic you should declare bean of type {@link KafkaMessageSender}
 *  in your application context. You can do it by putting the following code in your configuration class:
 *
 *  <pre>
 *    {@code @}Bean
 *     KafkaMessageSender<KafkaMessageSentEvent> testTopicKafkaMessageSender(Properties producerProperties,
 *                                                                           DemoServiceApiProperties demoServiceApiProperties,
 *                                                                           CommonMetricsCollector commonMetricsCollector) {
 *         // set appropriate serializer for value
 *         producerProperties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, EventSerializer.class.getName());
 *
 *         return new KafkaMessageSender<>(new KafkaProducer<>(producerProperties), demoServiceApiProperties.getMessageSentEventsTopicName(), commonMetricsCollector);
 *     }
 *  </pre>
 *
 *  In this example you are going to send:
 *  <ul>
 *      <li>Events of type{@code KafkaMessageSentEvent}</li>
 *      <li>In topic designated by {@code demoServiceApiProperties.getMessageSentEventsTopicName()}</li>
 *      <li>Using custom serializer of events {@code EventSerializer.class}</li>
 *  </ul>
 */
@Slf4j
public class KafkaMessageSender<T> {
    /**
     * Producer that is configured for sending messages of type {@link T}
     */
    private final KafkaProducer<String, T> producer;
    /**
     * Topic to send to
     */
    private final String topicName;

    /**
     * Important metrics. Integrated with Micrometer
     */
    private final CommonMetricsCollector commonMetricsCollector;

    /**
     * Logger for "noticeable" (important) events
     */
    @InjectEventLogger
    @SuppressWarnings("unused")
    private EventLogger eventLogger;

    public KafkaMessageSender(KafkaProducer<String, T> producer,
                              String topicName,
                              CommonMetricsCollector commonMetricsCollector) {
        this.commonMetricsCollector = commonMetricsCollector;
        this.producer = producer;
        this.topicName = topicName;
    }

    /**
     * Sends a single message to the topic
     */
    public void sendMessage(T message) {
        ProducerRecord<String, T> record = new ProducerRecord<>(topicName, message);
        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                eventLogger.warn(CommonNotableEvents.W_KAFKA_SEND_MESSAGE_FAILED, topicName);
                return;
            }
            log.debug("Message sent to Kafka topic: {}, event: {}", topicName, message);
            commonMetricsCollector.countKafkaMessageSent(topicName);
        });
    }
}
