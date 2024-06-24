package io.tech1.framework.incidents.domain.system;

import io.tech1.framework.foundation.domain.base.Username;
import io.tech1.framework.incidents.domain.AbstractIncident;
import io.tech1.framework.incidents.domain.Incident;

public record IncidentSystemResetServerCompleted(
        Username username
) implements AbstractIncident {

    public static IncidentSystemResetServerCompleted testsHardcoded() {
        return new IncidentSystemResetServerCompleted(
                Username.testsHardcoded()
        );
    }

    @Override
    public Incident getPlainIncident() {
        var incident = new Incident("Reset Server Completed");
        incident.addUsername(this.username);
        return incident;
    }
}
