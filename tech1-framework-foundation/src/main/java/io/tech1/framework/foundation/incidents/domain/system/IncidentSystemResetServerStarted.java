package io.tech1.framework.foundation.incidents.domain.system;

import io.tech1.framework.foundation.domain.base.Username;
import io.tech1.framework.foundation.incidents.domain.Incident;
import io.tech1.framework.foundation.incidents.domain.AbstractIncident;

public record IncidentSystemResetServerStarted(
        Username username
) implements AbstractIncident {

    public static IncidentSystemResetServerStarted testsHardcoded() {
        return new IncidentSystemResetServerStarted(
                Username.testsHardcoded()
        );
    }

    @Override
    public Incident getPlainIncident() {
        var incident = new Incident("Reset Server Started");
        incident.addUsername(this.username);
        return incident;
    }
}
