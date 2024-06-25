package io.tech1.framework.incidents.domain.authetication;

import io.tech1.framework.foundation.domain.base.UsernamePasswordCredentials;
import io.tech1.framework.foundation.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.foundation.domain.properties.base.SecurityJwtIncidentType;
import io.tech1.framework.incidents.domain.AbstractIncident;
import io.tech1.framework.incidents.domain.Incident;

public record IncidentAuthenticationLoginFailureUsernameMaskedPassword(
        UsernamePasswordCredentials credentials,
        UserRequestMetadata userRequestMetadata
) implements AbstractIncident {

    @Override
    public Incident getPlainIncident() {
        return new Incident(
                SecurityJwtIncidentType.AUTHENTICATION_LOGIN_FAILURE_USERNAME_MASKED_PASSWORD,
                this.credentials,
                this.userRequestMetadata
        );
    }
}
