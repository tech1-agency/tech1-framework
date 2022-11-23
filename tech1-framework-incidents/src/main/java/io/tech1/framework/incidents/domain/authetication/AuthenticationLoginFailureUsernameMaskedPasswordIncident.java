package io.tech1.framework.incidents.domain.authetication;

import io.tech1.framework.domain.base.Username;
import lombok.Data;

import static io.tech1.framework.domain.utilities.strings.MaskUtility.mask5;

// Lombok
@Data(staticConstructor = "of")
public class AuthenticationLoginFailureUsernameMaskedPasswordIncident {
    private final Username username;
    private final String password;

    public AuthenticationLoginFailureUsernameMaskedPasswordIncident(
            Username username,
            String password
    ) {
        this.username = username;
        this.password = mask5(password);
    }
}
