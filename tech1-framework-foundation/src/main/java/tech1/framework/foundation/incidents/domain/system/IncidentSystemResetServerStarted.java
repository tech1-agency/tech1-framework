package tech1.framework.foundation.incidents.domain.system;

import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.incidents.domain.Incident;
import tech1.framework.foundation.incidents.domain.AbstractIncident;

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
