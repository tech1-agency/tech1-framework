package io.tech1.framework.iam.domain.dto.responses;

import io.tech1.framework.iam.domain.jwt.JwtAccessToken;
import io.tech1.framework.iam.domain.jwt.JwtRefreshToken;

public record ResponseRefreshTokens(
        JwtAccessToken accessToken,
        JwtRefreshToken refreshToken
) {

    public static ResponseRefreshTokens random() {
        return new ResponseRefreshTokens(JwtAccessToken.random(), JwtRefreshToken.random());
    }
}
