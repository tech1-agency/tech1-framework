package io.tech1.framework.incidents.domain.authetication;

import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;

import static io.tech1.framework.domain.utilities.strings.MaskUtility.mask5;

public record IncidentAuthenticationLoginFailureUsernameMaskedPassword(
        Username username,
        Password password
) {

    public IncidentAuthenticationLoginFailureUsernameMaskedPassword(
            Username username,
            Password password
    ) {
        this.username = username;
        this.password = Password.of(mask5(password.value()));
    }
}
