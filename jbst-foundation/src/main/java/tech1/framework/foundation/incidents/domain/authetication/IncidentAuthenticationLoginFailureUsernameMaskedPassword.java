package tech1.framework.foundation.incidents.domain.authetication;

import tech1.framework.foundation.domain.base.UsernamePasswordCredentials;
import tech1.framework.foundation.domain.http.requests.UserRequestMetadata;
import tech1.framework.foundation.domain.properties.base.SecurityJwtIncidentType;
import tech1.framework.foundation.incidents.domain.AbstractIncident;
import tech1.framework.foundation.incidents.domain.Incident;

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
