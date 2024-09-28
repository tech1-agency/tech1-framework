package tech1.framework.iam.domain.dto.responses;

import tech1.framework.iam.domain.jwt.JwtAccessToken;
import tech1.framework.iam.domain.jwt.JwtRefreshToken;

public record ResponseRefreshTokens(
        JwtAccessToken accessToken,
        JwtRefreshToken refreshToken
) {

    public static ResponseRefreshTokens random() {
        return new ResponseRefreshTokens(JwtAccessToken.random(), JwtRefreshToken.random());
    }
}
