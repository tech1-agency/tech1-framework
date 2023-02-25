package io.tech1.framework.incidents.events.subscribers;

import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.throwable.IncidentThrowable;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

public interface IncidentSubscriber {
    @Async
    @EventListener
    void onEvent(Incident incident);
    @Async
    @EventListener
    void onEvent(IncidentThrowable incidentThrowable);
}
