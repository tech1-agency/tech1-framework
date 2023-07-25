package io.tech1.framework.incidents.domain.authetication;

import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;

public record IncidentAuthenticationLoginFailureUsernamePassword(
        Username username,
        Password password
) {
}
