package com.wine.to.up.commonlib.metrics;

import com.wine.to.up.commonlib.logging.NotableEvent;
import io.micrometer.core.instrument.Metrics;
import io.prometheus.client.Counter;
import org.springframework.beans.factory.annotation.Value;

/**
 * This Class expose methods for recording specific metrics
 * It changes metrics of Micrometer and Prometheus simultaneously
 * Micrometer's metrics exposed at /actuator/prometheus
 * Prometheus' metrics exposed at /metrics-prometheus
 */
public class CommonMetricsCollector {
    private static final String SERVICE_NAME_LABEL = "service";
    private static final String KAFKA_TOPIC_NAME_LABEL = "topic";
    private static final String EVENTS_TYPE_LABEL = "type";

//    private static final String SAGA_TEMPLATE_STEP_EXECUTED_TITLE = "saga_template_step_executed";
//    private static final Summary prometheusSagaTemplateStepExecutedSummary = Summary.build()
//            .name(SAGA_TEMPLATE_STEP_EXECUTED_TITLE)
//            .help("Saga's step execution summary")
//            .labelNames("saga_name", "step_name")
//            .register();

    private static final String KAFKA_MESSAGE_PRODUCED = "kafka_message_produced_total";
    private static final Counter kafkaMessageProduced = Counter.build()
            .name(KAFKA_MESSAGE_PRODUCED)
            .help("Number of produced messages by topics")
            .labelNames(SERVICE_NAME_LABEL, KAFKA_TOPIC_NAME_LABEL)
            .register();

    private static final String EVENTS = "events_total";
    private static final Counter prometheusEventsCounter = Counter.build()
            .name(EVENTS)
            .help("Amount of occurred events")
            .labelNames(SERVICE_NAME_LABEL, EVENTS_TYPE_LABEL)
            .register();

    @Value("${spring.application.name}")
    private String serviceName;


//    public void recordSagaInstanceStep(String sagaName, String stepName, double timeExecution) {
//        Metrics.summary(SAGA_TEMPLATE_STEP_EXECUTED_TITLE,
//                SAGA_NAME_LABEL, sagaName,
//                STEP_NAME_LABEL, stepName
//        ).record(timeExecution);
//        prometheusSagaTemplateStepExecutedSummary.labels(sagaName, stepName).observe(timeExecution);
//    }


    public void countKafkaMessageSent(String topicName) {
        Metrics.counter(KAFKA_MESSAGE_PRODUCED, SERVICE_NAME_LABEL, serviceName, KAFKA_TOPIC_NAME_LABEL, topicName).increment();
        kafkaMessageProduced.labels(serviceName, topicName).inc();
    }

    public void countEvent(NotableEvent event) {
        Metrics.counter(EVENTS, SERVICE_NAME_LABEL, serviceName, EVENTS_TYPE_LABEL, event.getName()).increment();
        prometheusEventsCounter.labels(serviceName, event.getName()).inc();
    }
}
