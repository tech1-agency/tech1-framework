package io.tech1.framework.b2b.mongodb.security.jwt.domain.session;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.base.Username;

public record Session(
        Username username,
        JwtRefreshToken refreshToken
) {
}

