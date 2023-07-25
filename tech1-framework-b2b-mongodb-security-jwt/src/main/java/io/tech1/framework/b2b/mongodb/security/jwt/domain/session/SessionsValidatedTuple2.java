package io.tech1.framework.b2b.mongodb.security.jwt.domain.session;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUserSession;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.tuples.Tuple2;

import java.util.List;

public record SessionsValidatedTuple2(
        List<Tuple2<Username, DbUserSession>> expiredSessions,
        List<String> expiredOrInvalidSessionIds
) {
}
