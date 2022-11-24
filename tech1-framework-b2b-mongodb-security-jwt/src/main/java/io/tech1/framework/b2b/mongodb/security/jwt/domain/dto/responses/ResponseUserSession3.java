package io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUserSession;
import io.tech1.framework.domain.base.Username;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class ResponseUserSession3 {
    private final Username who;
    private final String where;
    private final String what;
    private final String ipAddress;

    public ResponseUserSession3(DbUserSession session) {
        assertNonNullOrThrow(session, invalidAttribute("ResponseUserSession3.session"));
        this.who = session.getUsername();
        var requestMetadata = session.getRequestMetadata();
        this.where = requestMetadata.getWhereTuple3().getC();
        this.what = requestMetadata.getWhatTuple2().getB();
        this.ipAddress = requestMetadata.getWhereTuple3().getA();
    }
}
