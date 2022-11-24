package io.tech1.framework.b2b.mongodb.security.jwt.domain.events;

import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import lombok.Data;

import static io.tech1.framework.domain.utilities.strings.MaskUtility.mask5;

// Lombok
@Data(staticConstructor = "of")
public class EventAuthenticationLoginFailureUsernameMaskedPassword {
    private final Username username;
    private final Password password;

    public EventAuthenticationLoginFailureUsernameMaskedPassword(
            Username username,
            Password password
    ) {
        this.username = username;
        this.password = Password.of(mask5(password.getValue()));
    }
}
