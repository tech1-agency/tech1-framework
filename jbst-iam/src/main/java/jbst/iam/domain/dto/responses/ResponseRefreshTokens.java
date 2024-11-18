package jbst.iam.domain.dto.responses;

import jbst.iam.domain.jwt.JwtAccessToken;
import jbst.iam.domain.jwt.JwtRefreshToken;

public record ResponseRefreshTokens(
        JwtAccessToken accessToken,
        JwtRefreshToken refreshToken
) {

    public static ResponseRefreshTokens random() {
        return new ResponseRefreshTokens(JwtAccessToken.random(), JwtRefreshToken.random());
    }
}
