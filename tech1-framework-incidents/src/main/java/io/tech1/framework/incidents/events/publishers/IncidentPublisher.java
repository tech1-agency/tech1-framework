package io.tech1.framework.incidents.events.publishers;

import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.throwable.IncidentThrowable;

public interface IncidentPublisher {
    void publishIncident(Incident incident);
    void publishThrowable(IncidentThrowable incident);
    void publishThrowable(Throwable throwable);
}
