package tech1.framework.foundation.incidents.domain.authetication;

import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.properties.base.SecurityJwtIncidentType;
import tech1.framework.foundation.incidents.domain.AbstractIncident;
import tech1.framework.foundation.incidents.domain.Incident;

public record IncidentAuthenticationLogoutMin(
        Username username
) implements AbstractIncident {

    @Override
    public Incident getPlainIncident() {
        return new Incident(
                SecurityJwtIncidentType.AUTHENTICATION_LOGOUT_MIN,
                this.username
        );
    }
}
