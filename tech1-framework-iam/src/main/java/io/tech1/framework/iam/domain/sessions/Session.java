package io.tech1.framework.iam.domain.sessions;

import io.tech1.framework.iam.domain.jwt.JwtAccessToken;
import io.tech1.framework.iam.domain.jwt.JwtRefreshToken;
import tech1.framework.foundation.domain.base.Username;

public record Session(
        Username username,
        JwtAccessToken accessToken,
        JwtRefreshToken refreshToken
) {
}

