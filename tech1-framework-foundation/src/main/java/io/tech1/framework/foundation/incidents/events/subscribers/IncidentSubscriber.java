package io.tech1.framework.foundation.incidents.events.subscribers;

import io.tech1.framework.foundation.incidents.domain.Incident;
import io.tech1.framework.foundation.incidents.domain.system.IncidentSystemResetServerCompleted;
import io.tech1.framework.foundation.incidents.domain.system.IncidentSystemResetServerStarted;
import org.springframework.context.event.EventListener;

public interface IncidentSubscriber {
    @EventListener
    void onEvent(IncidentSystemResetServerStarted incident);
    @EventListener
    void onEvent(IncidentSystemResetServerCompleted incident);

    @EventListener
    void onEvent(Incident incident);
}
