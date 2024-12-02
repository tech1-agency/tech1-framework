package jbst.foundation.incidents.domain.registration;

import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.properties.base.SecurityJwtIncidentType;
import jbst.foundation.incidents.domain.AbstractIncident;
import jbst.foundation.incidents.domain.Incident;

import static jbst.foundation.incidents.domain.IncidentAttributes.Keys.EXCEPTION;

public record IncidentRegistration0Failure(
        Username username,
        String exception
) implements AbstractIncident {

    @Override
    public Incident getPlainIncident() {
        var incident = new Incident(
                SecurityJwtIncidentType.REGISTER0_FAILURE,
                this.username
        );
        incident.add(EXCEPTION, this.exception);
        return incident;
    }
}