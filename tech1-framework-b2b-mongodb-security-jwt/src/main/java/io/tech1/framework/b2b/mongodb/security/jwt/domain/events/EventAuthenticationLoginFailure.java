package io.tech1.framework.b2b.mongodb.security.jwt.domain.events;

import io.tech1.framework.domain.base.Username;
import lombok.Data;

// Lombok
@Data
public class EventAuthenticationLoginFailure {
    private final Username username;
}
