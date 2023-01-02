package io.tech1.framework.incidents.events.publishers;

import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.throwable.IncidentThrowable;
import org.springframework.scheduling.annotation.Async;

public interface IncidentPublisher {
    @Async
    void publishIncident(Incident incident);
    @Async
    void publishThrowable(IncidentThrowable incident);
    @Async
    void publishThrowable(Throwable throwable);
}
