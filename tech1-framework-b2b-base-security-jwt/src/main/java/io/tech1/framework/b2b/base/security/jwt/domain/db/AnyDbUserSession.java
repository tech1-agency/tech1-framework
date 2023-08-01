package io.tech1.framework.b2b.base.security.jwt.domain.db;

import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;

import static io.tech1.framework.domain.constants.StringConstants.UNDEFINED;

public record AnyDbUserSession(
        boolean persisted,
        UserSessionId id,
        Username username,
        JwtAccessToken accessToken,
        JwtRefreshToken refreshToken,
        UserRequestMetadata metadata
) {

    public static AnyDbUserSession ofPersisted(
            UserSessionId id,
            Username username,
            JwtAccessToken accessToken,
            JwtRefreshToken refreshToken,
            UserRequestMetadata metadata
    ) {
        return new AnyDbUserSession(true, id, username, accessToken, refreshToken, metadata);
    }

    public static AnyDbUserSession ofNotPersisted(
            Username username,
            JwtAccessToken accessToken,
            JwtRefreshToken refreshToken,
            UserRequestMetadata metadata
    ) {
        return new AnyDbUserSession(false, new UserSessionId(UNDEFINED), username, accessToken, refreshToken, metadata);
    }
}
