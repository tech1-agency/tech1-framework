package io.tech1.framework.b2b.base.security.jwt.domain.events;

import io.tech1.framework.foundation.domain.base.Username;

public record EventAuthenticationLogout(
        Username username
) {
}
