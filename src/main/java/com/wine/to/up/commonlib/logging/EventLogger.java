package com.wine.to.up.commonlib.logging;

import com.wine.to.up.commonlib.metrics.CommonMetricsCollector;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;

/**
 * This is specialized version of logback logger.
 * Every time we log specific {@link CommonNotableEvents}
 * we record metric called @{code events_total}
 * with event type as label derived from name of event.
 *
 * <p>
 * Example of instance declaration
 * <pre>{@code
 * class SomeClass {
 *   @InjectEventLogger
 *   KPIEventsLogger logger;
 * }
 * }</pre>
 *
 * <p>
 * Whenever you want to log an event
 * call it as you would typically do with logback
 * {@link EventLogger#trace}
 * {@link EventLogger#debug}
 * {@link EventLogger#info}
 * {@link EventLogger#warn}
 * {@link EventLogger#error}
 */
@AllArgsConstructor
public class EventLogger {
    private final Logger log;
    private final CommonMetricsCollector metrics;

    public void trace(NotableEvent event, Object... payload) {
        log.trace(event.getTemplate(), payload); // todo sukhoa use Markers and add event name!
        metrics.countEvent(event);
    }

    public void debug(NotableEvent event, Object... payload) {
        log.debug(event.getTemplate(), payload); // todo sukhoa use Markers and add event name!
        metrics.countEvent(event);
    }

    public void info(NotableEvent event, Object... payload) {
        log.info(event.getTemplate(), payload); // todo sukhoa use Markers and add event name!
        metrics.countEvent(event);
    }

    public void warn(NotableEvent event, Object... payload) {
        log.warn(event.getTemplate(), payload); // todo sukhoa use Markers and add event name!
        metrics.countEvent(event);
    }

    public void error(NotableEvent event, Object... payload) {
        log.error(event.getTemplate(), payload); // todo sukhoa use Markers and add event name!
        metrics.countEvent(event);
    }
}
