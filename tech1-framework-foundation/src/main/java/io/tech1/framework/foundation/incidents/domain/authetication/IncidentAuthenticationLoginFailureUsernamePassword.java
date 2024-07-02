package io.tech1.framework.foundation.incidents.domain.authetication;

import io.tech1.framework.foundation.domain.base.UsernamePasswordCredentials;
import io.tech1.framework.foundation.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.foundation.domain.properties.base.SecurityJwtIncidentType;
import io.tech1.framework.foundation.incidents.domain.AbstractIncident;
import io.tech1.framework.foundation.incidents.domain.Incident;

public record IncidentAuthenticationLoginFailureUsernamePassword(
        UsernamePasswordCredentials credentials,
        UserRequestMetadata userRequestMetadata
) implements AbstractIncident {

    @Override
    public Incident getPlainIncident() {
        return new Incident(
                SecurityJwtIncidentType.AUTHENTICATION_LOGIN_FAILURE_USERNAME_PASSWORD,
                this.credentials,
                this.userRequestMetadata
        );
    }
}
