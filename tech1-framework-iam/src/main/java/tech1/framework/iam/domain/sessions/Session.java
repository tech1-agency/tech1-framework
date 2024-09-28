package tech1.framework.iam.domain.sessions;

import tech1.framework.iam.domain.jwt.JwtAccessToken;
import tech1.framework.iam.domain.jwt.JwtRefreshToken;
import tech1.framework.foundation.domain.base.Username;

public record Session(
        Username username,
        JwtAccessToken accessToken,
        JwtRefreshToken refreshToken
) {
}

