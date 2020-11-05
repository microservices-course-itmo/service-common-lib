package com.wine.to.up.commonlib.metrics;

import com.wine.to.up.commonlib.logging.NotableEvent;
import io.micrometer.core.instrument.Metrics;
import io.prometheus.client.Counter;

/**
 * This Class exposes methods for recording specific metrics
 * It changes metrics of Micrometer and Prometheus simultaneously
 * Micrometer's metrics exposed at /actuator/prometheus
 * Prometheus' metrics exposed at /metrics-prometheus
 */
public class CommonMetricsCollector {
    private static final String KAFKA_MESSAGE_PRODUCED = "kafka_message_produced_total";
    private static final String EVENTS = "events_total";

    private static final String KAFKA_TOPIC_NAME_LABEL = "topic";
    private static final String EVENTS_TYPE_LABEL = "type";

    private String serviceName;

//    private static final String SAGA_TEMPLATE_STEP_EXECUTED_TITLE = "saga_template_step_executed";
//    private static final Summary prometheusSagaTemplateStepExecutedSummary = Summary.build()
//            .name(SAGA_TEMPLATE_STEP_EXECUTED_TITLE)
//            .help("Saga's step execution summary")
//            .labelNames("saga_name", "step_name")
//            .register();

    private final Counter kafkaMessageProduced;

    private final Counter prometheusEventsCounter;

//    public void recordSagaInstanceStep(String sagaName, String stepName, double timeExecution) {
//        Metrics.summary(SAGA_TEMPLATE_STEP_EXECUTED_TITLE,
//                SAGA_NAME_LABEL, sagaName,
//                STEP_NAME_LABEL, stepName
//        ).record(timeExecution);
//        prometheusSagaTemplateStepExecutedSummary.labels(sagaName, stepName).observe(timeExecution);
//    }


    public CommonMetricsCollector(String serviceName) {
        this.serviceName = serviceName;

        this.kafkaMessageProduced = createAndRegisterCounter(
                KAFKA_MESSAGE_PRODUCED,
                "Number of produced messages by topics",
                KAFKA_TOPIC_NAME_LABEL
        );

        this.prometheusEventsCounter = createAndRegisterCounter(
                EVENTS,
                "Amount of occurred events",
                EVENTS_TYPE_LABEL
        );
    }

    public void countKafkaMessageSent(String topicName) {
        Metrics.counter(KAFKA_MESSAGE_PRODUCED, KAFKA_TOPIC_NAME_LABEL, topicName).increment();
        kafkaMessageProduced.labels(topicName).inc();
    }

    public void countEvent(NotableEvent event) {
        Metrics.counter(EVENTS, EVENTS_TYPE_LABEL, event.getName()).increment();
        prometheusEventsCounter.labels(event.getName()).inc();
    }

    private Counter createAndRegisterCounter(String name, String help, String... labels) {
        return Counter.build()
                .namespace(serviceName)
                .name(name)
                .help(help)
                .labelNames(labels)
                .register();
    }

}
