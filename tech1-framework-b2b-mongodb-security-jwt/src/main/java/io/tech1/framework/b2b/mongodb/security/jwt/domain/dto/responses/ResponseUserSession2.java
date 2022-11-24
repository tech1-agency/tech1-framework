package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUserSession;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.domain.tuples.TupleExceptionDetails;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

// Lombok
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ResponseUserSession2 {
    private final String id;

    private final boolean current;
    private final String activity;

    private final TupleExceptionDetails exception;

    private final String ipAddr;
    private final String country;
    private final String where;

    private final String browser;
    private final String what;

    public ResponseUserSession2(DbUserSession session, CookieRefreshToken cookieRefreshToken) {
        assertNonNullOrThrow(session, invalidAttribute("ResponseUserSession2.session"));
        assertNonNullOrThrow(cookieRefreshToken, invalidAttribute("ResponseUserSession2.cookieRefreshToken"));
        this.id = session.getId();

        this.current = cookieRefreshToken.getValue().equals(session.getJwtRefreshToken().getValue());
        if (this.current) {
            this.activity = "Current session";
        } else {
            this.activity = "—";
        }

        var requestMetadata = session.getRequestMetadata();
        this.exception = requestMetadata.getException();

        var whereTuple3 = requestMetadata.getWhereTuple3();
        this.ipAddr = whereTuple3.getA();
        this.country = whereTuple3.getB();
        this.where = whereTuple3.getC();

        var whatTuple2 = requestMetadata.getWhatTuple2();
        this.browser = whatTuple2.getA();
        this.what = whatTuple2.getB();
    }
}
