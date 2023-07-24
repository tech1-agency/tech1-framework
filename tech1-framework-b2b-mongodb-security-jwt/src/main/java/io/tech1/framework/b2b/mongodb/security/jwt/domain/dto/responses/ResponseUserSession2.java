package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUserSession;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.tuples.TupleExceptionDetails;

public record ResponseUserSession2(
        String id,
        Username who,
        boolean current,
        String activity,
        TupleExceptionDetails exception,
        String ipAddr,
        String countryFlag,
        String where,
        String browser,
        String what
) {

    public static ResponseUserSession2 of(DbUserSession session, CookieRefreshToken cookie) {
        var current = cookie.value().equals(session.getJwtRefreshToken().value());
        var activity = "";
        if (current) {
            activity = "Current session";
        } else {
            activity = "—";
        }

        var whereTuple3 = session.getRequestMetadata().getWhereTuple3();
        var whatTuple2 = session.getRequestMetadata().getWhatTuple2();

        return new ResponseUserSession2(
                session.getId(),
                session.getUsername(),
                current,
                activity,
                session.getRequestMetadata().getException(),
                whereTuple3.a(),
                whereTuple3.b(),
                whereTuple3.c(),
                whatTuple2.a(),
                whatTuple2.b()
        );
    }
}
