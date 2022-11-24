package io.tech1.framework.b2b.mongodb.security.jwt.domain.session;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUserSession;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.tuples.Tuple2;
import lombok.Data;

import java.util.List;

// Lombok
@Data(staticConstructor = "of")
public class SessionsValidatedTuple2 {
    private final List<Tuple2<Username, DbUserSession>> expiredSessions;
    private final List<String> expiredOrInvalidSessionIds;
}
