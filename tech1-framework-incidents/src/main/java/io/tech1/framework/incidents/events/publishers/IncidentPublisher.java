package io.tech1.framework.incidents.events.publishers;

import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.system.IncidentSystemResetServerCompleted;
import io.tech1.framework.incidents.domain.system.IncidentSystemResetServerStarted;

public interface IncidentPublisher {
    void publishResetServerStarted(IncidentSystemResetServerStarted incident);
    void publishResetServerCompleted(IncidentSystemResetServerCompleted incident);

    void publishIncident(Incident incident);

    default void publishThrowable(Throwable throwable) {
        this.publishIncident(new Incident(throwable));
    }
}
