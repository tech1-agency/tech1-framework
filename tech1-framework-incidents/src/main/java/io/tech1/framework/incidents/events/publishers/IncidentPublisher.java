package io.tech1.framework.incidents.events.publishers;

import io.tech1.framework.incidents.domain.Incident;
import io.tech1.framework.incidents.domain.system.IncidentSystemResetServerCompleted;
import io.tech1.framework.incidents.domain.system.IncidentSystemResetServerStarted;
import io.tech1.framework.incidents.domain.throwable.IncidentThrowable;

public interface IncidentPublisher {
    void publishResetServerStarted(IncidentSystemResetServerStarted incident);
    void publishResetServerCompleted(IncidentSystemResetServerCompleted incident);

    void publishIncident(Incident incident);

    void publishThrowable(IncidentThrowable incident);
    void publishThrowable(Throwable throwable);
}
