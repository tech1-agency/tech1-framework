package jbst.foundation.incidents.events.subscribers;

import jbst.foundation.incidents.domain.Incident;
import jbst.foundation.incidents.domain.system.IncidentSystemResetServerCompleted;
import jbst.foundation.incidents.domain.system.IncidentSystemResetServerStarted;
import org.springframework.context.event.EventListener;

public interface IncidentSubscriber {
    @EventListener
    void onEvent(IncidentSystemResetServerStarted incident);
    @EventListener
    void onEvent(IncidentSystemResetServerCompleted incident);

    @EventListener
    void onEvent(Incident incident);
}
