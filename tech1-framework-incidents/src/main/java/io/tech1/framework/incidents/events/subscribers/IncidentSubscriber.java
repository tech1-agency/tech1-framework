package io.tech1.framework.incidents.events.subscribers;

import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.throwable.IncidentThrowable;
import org.springframework.context.event.EventListener;

public interface IncidentSubscriber {
    @EventListener
    void onEvent(Incident incident);
    @EventListener
    void onEvent(IncidentThrowable incidentThrowable);
}
