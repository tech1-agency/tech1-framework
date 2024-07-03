package io.tech1.framework.foundation.incidents.domain.authetication;

import io.tech1.framework.foundation.domain.base.Username;
import io.tech1.framework.foundation.domain.properties.base.SecurityJwtIncidentType;
import io.tech1.framework.foundation.incidents.domain.AbstractIncident;
import io.tech1.framework.foundation.incidents.domain.Incident;

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
