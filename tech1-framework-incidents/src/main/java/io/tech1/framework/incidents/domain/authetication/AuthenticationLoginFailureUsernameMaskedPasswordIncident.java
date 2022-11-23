package io.tech1.framework.incidents.domain.authetication;

import io.tech1.framework.domain.base.Username;
import lombok.Data;

import static io.tech1.framework.domain.utilities.strings.MaskUtility.mask5;

// Lombok
@Data
public class AuthenticationLoginFailureUsernameMaskedPasswordIncident {
    private final Username username;
    private final String password;

    public static AuthenticationLoginFailureUsernameMaskedPasswordIncident of(
            Username username,
            String password
    ) {
        return new AuthenticationLoginFailureUsernameMaskedPasswordIncident(
                username,
                mask5(password)
        );
    }
}
