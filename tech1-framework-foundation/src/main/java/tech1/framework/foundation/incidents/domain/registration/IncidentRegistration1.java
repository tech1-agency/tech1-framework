package tech1.framework.foundation.incidents.domain.registration;

import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.properties.base.SecurityJwtIncidentType;
import tech1.framework.foundation.incidents.domain.AbstractIncident;
import tech1.framework.foundation.incidents.domain.Incident;

public record IncidentRegistration1(
        Username username
) implements AbstractIncident {

    @Override
    public Incident getPlainIncident() {
        return new Incident(
                SecurityJwtIncidentType.REGISTER1,
                this.username
        );
    }
}
