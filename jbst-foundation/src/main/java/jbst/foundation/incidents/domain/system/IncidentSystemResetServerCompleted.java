package jbst.foundation.incidents.domain.system;

import jbst.foundation.domain.base.Username;
import jbst.foundation.incidents.domain.AbstractIncident;
import jbst.foundation.incidents.domain.Incident;

public record IncidentSystemResetServerCompleted(
        Username username
) implements AbstractIncident {

    public static IncidentSystemResetServerCompleted hardcoded() {
        return new IncidentSystemResetServerCompleted(
                Username.hardcoded()
        );
    }

    @Override
    public Incident getPlainIncident() {
        var incident = new Incident("Reset Server Completed");
        incident.addUsername(this.username);
        return incident;
    }
}
