package io.tech1.framework.incidents.domain.authetication;

import io.tech1.framework.domain.base.UsernamePasswordCredentials;

public record IncidentAuthenticationLoginFailureUsernameMaskedPassword(
        UsernamePasswordCredentials credentials
) {
}
