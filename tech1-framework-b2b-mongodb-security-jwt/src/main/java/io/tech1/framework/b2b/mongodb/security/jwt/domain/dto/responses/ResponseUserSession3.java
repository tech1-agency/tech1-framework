package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUserSession;
import io.tech1.framework.domain.base.Username;

public record ResponseUserSession3(
        Username who,
        String where,
        String what,
        String ipAddress
) {

    public ResponseUserSession3(
            DbUserSession session
    ) {
        this(
                session.getUsername(),
                session.getRequestMetadata().getWhereTuple3().c(),
                session.getRequestMetadata().getWhereTuple3().b(),
                session.getRequestMetadata().getWhereTuple3().a()
        );
    }
}
