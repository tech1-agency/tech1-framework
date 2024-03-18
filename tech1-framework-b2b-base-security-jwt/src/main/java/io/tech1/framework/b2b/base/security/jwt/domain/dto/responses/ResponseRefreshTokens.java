package io.tech1.framework.b2b.base.security.jwt.domain.dto.responses;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;

public record ResponseRefreshTokens(
        JwtAccessToken accessToken,
        JwtRefreshToken refreshToken
) {

    public static ResponseRefreshTokens random() {
        return new ResponseRefreshTokens(JwtAccessToken.random(), JwtRefreshToken.random());
    }
}
