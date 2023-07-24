package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUserSession;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.tuples.TupleExceptionDetails;
import lombok.Data;

// Lombok
@Data
public class ResponseUserSession2 {
    private final String id;

    private final Username who;

    private final boolean current;
    private final String activity;

    private final TupleExceptionDetails exception;

    private final String ipAddr;
    private final String countryFlag;
    private final String where;

    private final String browser;
    private final String what;

    public ResponseUserSession2(DbUserSession session, CookieRefreshToken cookie) {
        this.id = session.getId();

        this.who = session.getUsername();

        this.current = cookie.value().equals(session.getJwtRefreshToken().value());
        if (this.current) {
            this.activity = "Current session";
        } else {
            this.activity = "—";
        }

        var requestMetadata = session.getRequestMetadata();
        this.exception = requestMetadata.getException();

        var whereTuple3 = requestMetadata.getWhereTuple3();
        this.ipAddr = whereTuple3.a();
        this.countryFlag = whereTuple3.b();
        this.where = whereTuple3.c();

        var whatTuple2 = requestMetadata.getWhatTuple2();
        this.browser = whatTuple2.a();
        this.what = whatTuple2.b();
    }
}
