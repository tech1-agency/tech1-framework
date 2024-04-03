package io.tech1.framework.incidents.domain.authetication;

import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.properties.base.SecurityJwtIncidentType;
import io.tech1.framework.incidents.domain.AbstractIncident;
import io.tech1.framework.incidents.domain.Incident;

public record IncidentAuthenticationLogoutFull(
        Username username,
        UserRequestMetadata userRequestMetadata
) implements AbstractIncident {

    @Override
    public Incident getPlainIncident() {
        return new Incident(
                SecurityJwtIncidentType.AUTHENTICATION_LOGOUT,
                this.username,
                this.userRequestMetadata
        );
    }
}
