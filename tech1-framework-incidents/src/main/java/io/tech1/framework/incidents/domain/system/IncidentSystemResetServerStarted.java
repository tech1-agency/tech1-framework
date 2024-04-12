package io.tech1.framework.incidents.domain.system;

import io.tech1.framework.domain.base.Username;
import io.tech1.framework.incidents.domain.AbstractIncident;
import io.tech1.framework.incidents.domain.Incident;

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
