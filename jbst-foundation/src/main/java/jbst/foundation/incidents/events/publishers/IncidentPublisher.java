package jbst.foundation.incidents.events.publishers;

import jbst.foundation.incidents.domain.Incident;
import jbst.foundation.incidents.domain.system.IncidentSystemResetServerCompleted;
import jbst.foundation.incidents.domain.system.IncidentSystemResetServerStarted;

public interface IncidentPublisher {
    void publishResetServerStarted(IncidentSystemResetServerStarted incident);
    void publishResetServerCompleted(IncidentSystemResetServerCompleted incident);

    void publishIncident(Incident incident);

    default void publishThrowable(Throwable throwable) {
        this.publishIncident(new Incident(throwable));
    }
}
