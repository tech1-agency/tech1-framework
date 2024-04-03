package io.tech1.framework.incidents.domain.authetication;

import io.tech1.framework.domain.base.UsernamePasswordCredentials;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;

public record IncidentAuthenticationLoginFailureUsernamePassword(
        UsernamePasswordCredentials credentials,
        UserRequestMetadata userRequestMetadata
) {
}
