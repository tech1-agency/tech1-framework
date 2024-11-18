package jbst.iam.domain.dto.responses;

import jbst.iam.domain.identifiers.UserSessionId;
import jbst.iam.domain.jwt.JwtAccessToken;
import jbst.iam.domain.jwt.RequestAccessToken;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.http.requests.UserRequestMetadata;
import tech1.framework.foundation.domain.time.TimeAgo;
import tech1.framework.foundation.domain.time.TimeAmount;
import tech1.framework.foundation.domain.tuples.TupleExceptionDetails;
import tech1.framework.foundation.utilities.time.TimestampUtility;

import java.util.Comparator;

import static java.util.Comparator.comparing;

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

    public static final Comparator<ResponseUserSession2> USERS_SESSIONS = comparing(ResponseUserSession2::current).reversed()
            .thenComparing(ResponseUserSession2::where);

    public static final Comparator<ResponseUserSession2> ACTIVE_SESSIONS_AS_SUPERADMIN = comparing(ResponseUserSession2::current).reversed()
            .thenComparing((ResponseUserSession2 session) -> session.who().value())
            .thenComparing(ResponseUserSession2::where);

    public static final Comparator<ResponseUserSession2> INACTIVE_SESSIONS_AS_SUPERADMIN = comparing((ResponseUserSession2 session) -> session.who().value())
            .thenComparing(ResponseUserSession2::where);

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
