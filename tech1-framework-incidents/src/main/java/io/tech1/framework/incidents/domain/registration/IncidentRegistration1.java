package io.tech1.framework.incidents.domain.registration;

import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.properties.base.SecurityJwtIncidentType;
import io.tech1.framework.incidents.domain.AbstractIncident;
import io.tech1.framework.incidents.domain.Incident;

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
