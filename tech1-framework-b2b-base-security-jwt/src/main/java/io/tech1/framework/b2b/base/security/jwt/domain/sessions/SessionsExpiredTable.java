package io.tech1.framework.b2b.base.security.jwt.domain.sessions;

import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.tuples.Tuple4;

import java.util.List;

public record SessionsExpiredTable(
        List<Tuple4<Username, JwtAccessToken, JwtRefreshToken, UserRequestMetadata>> expiredSessions,
        List<UserSessionId> expiredOrInvalidSessionIds
) {
}
