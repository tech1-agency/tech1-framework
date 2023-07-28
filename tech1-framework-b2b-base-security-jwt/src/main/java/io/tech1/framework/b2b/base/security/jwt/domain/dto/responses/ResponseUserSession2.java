package io.tech1.framework.b2b.base.security.jwt.domain.dto.responses;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.tuples.TupleExceptionDetails;

public record ResponseUserSession2(
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
            Username username,
            UserRequestMetadata requestMetadata,
            JwtRefreshToken jwtRefreshToken,
            CookieRefreshToken cookie
    ) {
        var current = cookie.value().equals(jwtRefreshToken.value());
        var activity = "";
        if (current) {
            activity = "Current session";
        } else {
            activity = "—";
        }

        var whereTuple3 = requestMetadata.getWhereTuple3();
        var whatTuple2 = requestMetadata.getWhatTuple2();

        return new ResponseUserSession2(
                username,
                current,
                activity,
                requestMetadata.getException(),
                whereTuple3.a(),
                whereTuple3.b(),
                whereTuple3.c(),
                whatTuple2.a(),
                whatTuple2.b()
        );
    }
}
