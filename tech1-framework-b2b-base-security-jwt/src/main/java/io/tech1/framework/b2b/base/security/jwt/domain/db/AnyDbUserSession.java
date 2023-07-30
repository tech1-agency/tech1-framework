package io.tech1.framework.b2b.base.security.jwt.domain.db;

import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;

public record AnyDbUserSession(
        UserSessionId id,
        Username username,
        UserRequestMetadata metadata,
        JwtRefreshToken jwtRefreshToken
) {
}
