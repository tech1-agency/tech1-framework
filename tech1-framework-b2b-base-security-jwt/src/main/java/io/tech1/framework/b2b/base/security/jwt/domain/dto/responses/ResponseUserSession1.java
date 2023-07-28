package io.tech1.framework.b2b.base.security.jwt.domain.dto.responses;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;

public record ResponseUserSession1(
        JwtRefreshToken refreshToken
) {
}
