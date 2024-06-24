package io.tech1.framework.b2b.base.security.jwt.domain.dto.responses;

import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestAccessToken;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.time.TimeAgo;
import io.tech1.framework.domain.time.TimeAmount;
import io.tech1.framework.domain.tuples.TupleExceptionDetails;
import io.tech1.framework.foundation.utilities.time.TimestampUtility;

public record ResponseUserSession2(
        UserSessionId id,
        Username who,
        boolean current,
        String activity,
        TimeAgo when,
        TupleExceptionDetails exception,
        String country,
        String ipAddr,
        String countryFlag,
        String where,
        String browser,
        String what
) {

    public static ResponseUserSession2 of(
            UserSessionId id,
            long updatedAt,
            Username username,
            RequestAccessToken requestAccessToken,
            JwtAccessToken accessToken,
            UserRequestMetadata metadata
    ) {
        var current = requestAccessToken.value().equals(accessToken.value());
        var activity = current ? "Current session" : "—";

        var whereTuple3 = metadata.getWhereTuple3();
        var whatTuple2 = metadata.getWhatTuple2();
        var country = metadata.getGeoLocation().getCountry();

        return new ResponseUserSession2(
                id,
                username,
                current,
                activity,
                new TimeAgo(updatedAt),
                metadata.getException(),
                country,
                whereTuple3.a(),
                whereTuple3.b(),
                whereTuple3.c(),
                whatTuple2.a(),
                whatTuple2.b()
        );
    }

    public static ResponseUserSession2 testsHardcodedCurrent() {
        var token = "PFRL63OtcEKKy0hb7UjE";
        return of(
                UserSessionId.testsHardcoded(),
                TimestampUtility.getCurrentTimestamp(),
                Username.testsHardcoded(),
                new RequestAccessToken(token),
                new JwtAccessToken(token),
                UserRequestMetadata.valid()
        );
    }

    public static ResponseUserSession2 random() {
        return of(
                UserSessionId.random(),
                TimestampUtility.getCurrentTimestamp() - TimeAmount.random().toMillis(),
                Username.testsHardcoded(),
                RequestAccessToken.random(),
                JwtAccessToken.random(),
                UserRequestMetadata.testData()
        );
    }
}
