package io.tech1.framework.b2b.base.security.jwt.domain.sessions;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.base.Username;

public record Session(
        Username username,
        JwtRefreshToken refreshToken
) {
}

