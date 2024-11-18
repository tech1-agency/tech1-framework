package jbst.foundation.incidents.domain.system;

import jbst.foundation.domain.base.Username;
import jbst.foundation.incidents.domain.Incident;
import jbst.foundation.incidents.domain.AbstractIncident;

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
