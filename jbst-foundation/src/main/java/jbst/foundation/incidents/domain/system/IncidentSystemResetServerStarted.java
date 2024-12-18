package jbst.foundation.incidents.domain.system;

import jbst.foundation.domain.base.Username;
import jbst.foundation.incidents.domain.AbstractIncident;
import jbst.foundation.incidents.domain.Incident;

public record IncidentSystemResetServerStarted(
        Username username
) implements AbstractIncident {

    public static IncidentSystemResetServerStarted hardcoded() {
        return new IncidentSystemResetServerStarted(
                Username.hardcoded()
        );
    }

    @Override
    public Incident getPlainIncident() {
        var incident = new Incident("Reset Server Started");
        incident.addUsername(this.username);
        return incident;
    }
}
