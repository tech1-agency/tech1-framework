package io.tech1.framework.b2b.mongodb.security.jwt.domain.events;

import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import lombok.Data;

// Lombok
@Data(staticConstructor = "of")
public class EventAuthenticationLoginFailureUsernamePassword {
    private final Username username;
    private final Password password;
}
