package io.tech1.framework.b2b.base.security.jwt.domain.dto.responses;

import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.tuples.TupleExceptionDetails;

public record ResponseUserSession2(
        UserSessionId id,
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

    public static ResponseUserSession2 of(
            UserSessionId id,
            Username username,
            CookieAccessToken cookie,
            JwtAccessToken accessToken,
            UserRequestMetadata metadata
    ) {
        var current = cookie.value().equals(accessToken.value());
        var activity = "";
        if (current) {
            System.out.println("cookie.value(): " + cookie.value());
            activity = "Current session";
        } else {
            activity = "—";
        }

        var whereTuple3 = metadata.getWhereTuple3();
        var whatTuple2 = metadata.getWhatTuple2();

        return new ResponseUserSession2(
                id,
                username,
                current,
                activity,
                metadata.getException(),
                whereTuple3.a(),
                whereTuple3.b(),
                whereTuple3.c(),
                whatTuple2.a(),
                whatTuple2.b()
        );
    }
}
