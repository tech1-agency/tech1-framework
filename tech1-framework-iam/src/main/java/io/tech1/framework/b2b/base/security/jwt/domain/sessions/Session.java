package io.tech1.framework.b2b.base.security.jwt.domain.sessions;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.foundation.domain.base.Username;

public record Session(
        Username username,
        JwtAccessToken accessToken,
        JwtRefreshToken refreshToken
) {
}

