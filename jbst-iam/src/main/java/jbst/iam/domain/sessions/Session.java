package jbst.iam.domain.sessions;

import jbst.iam.domain.jwt.JwtAccessToken;
import jbst.iam.domain.jwt.JwtRefreshToken;
import tech1.framework.foundation.domain.base.Username;

public record Session(
        Username username,
        JwtAccessToken accessToken,
        JwtRefreshToken refreshToken
) {
}

