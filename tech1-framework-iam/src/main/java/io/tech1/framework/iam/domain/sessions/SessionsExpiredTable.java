package io.tech1.framework.iam.domain.sessions;

import io.tech1.framework.iam.domain.identifiers.UserSessionId;
import io.tech1.framework.iam.domain.jwt.JwtRefreshToken;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.http.requests.UserRequestMetadata;
import tech1.framework.foundation.domain.tuples.Tuple3;

import java.util.List;
import java.util.Set;

public record SessionsExpiredTable(
        List<Tuple3<Username, JwtRefreshToken, UserRequestMetadata>> expiredSessions,
        Set<UserSessionId> expiredOrInvalidSessionIds
) {
}
