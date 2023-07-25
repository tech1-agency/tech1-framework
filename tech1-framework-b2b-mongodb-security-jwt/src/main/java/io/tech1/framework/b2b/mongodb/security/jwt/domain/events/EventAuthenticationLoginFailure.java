package io.tech1.framework.b2b.mongodb.security.jwt.domain.events;

import io.tech1.framework.domain.base.Username;

public record EventAuthenticationLoginFailure(
        Username username
) {
}
