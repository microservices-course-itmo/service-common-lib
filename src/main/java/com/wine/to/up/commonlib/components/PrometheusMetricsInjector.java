package com.wine.to.up.commonlib.components;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.config.MeterFilter;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.MetricsServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * We have Micrometer as main metrics generator which exposed its results at using actuator
 * Since its needed to make alternative metrics generation we use official Prometheus Java Client for it
 * Here we inject their servlet into our HTTP server at specific url
 * This servlet generates content in prometheus format by accessing specified url
 * <p>
 * {@link PrometheusMetricsInjector#injectPrometheusServlet}
 * <p>
 * More:
 * https://github.com/prometheus/client_java#http
 */
@Component
public class PrometheusMetricsInjector {
    /**
     * Service name tag
     */
    private static final String SERVICE_NAME_TAG_NAME = "service";

    /**
     * Service name
     */
    @Value("${spring.application.name}")
    private String serviceName;

    /**
     * This method are invoked by Spring at Bean construction phase
     * The only purpose is to create servlet and return it for further injection
     *
     * @return Bean injection for HTTP server
     */
    @Bean
    public ServletRegistrationBean injectPrometheusServlet() {

        // URL at which we expose alternative generated metrics
        var targetUrl = "/metrics-prometheus";

        // Create servlet bound to global metrics registry
        var prometheusServlet = new MetricsServlet(CollectorRegistry.defaultRegistry);

        // Inject previously created servlet into HTTP server
        return new ServletRegistrationBean(prometheusServlet, targetUrl);
    }

    /**
     * This method are invoked by Spring at Bean construction phase
     * The only purpose is to create {@link MeterFilter} and return it for further injection
     *
     * @return Bean injection for {@link MeterFilter}
     */
    @Bean
    public MeterFilter injectMeterFilter() {
        return new MeterFilter() {
            @Override
            public Meter.Id map(Meter.Id id) {
                return id.withTags(Tags.concat(Tags.of(SERVICE_NAME_TAG_NAME, serviceName)));
            }
        };
    }
}
