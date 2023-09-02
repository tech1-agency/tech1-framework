package io.tech1.framework.b2b.base.security.jwt.domain.db;

import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;

public record UserSession(
        boolean persisted,
        UserSessionId id,
        Username username,
        JwtAccessToken accessToken,
        JwtRefreshToken refreshToken,
        UserRequestMetadata metadata
) {

    public static UserSession ofPersisted(
            UserSessionId id,
            Username username,
            JwtAccessToken accessToken,
            JwtRefreshToken refreshToken,
            UserRequestMetadata metadata
    ) {
        return new UserSession(true, id, username, accessToken, refreshToken, metadata);
    }

    public static UserSession ofNotPersisted(
            Username username,
            JwtAccessToken accessToken,
            JwtRefreshToken refreshToken,
            UserRequestMetadata metadata
    ) {
        return new UserSession(false, UserSessionId.undefined(), username, accessToken, refreshToken, metadata);
    }
}
