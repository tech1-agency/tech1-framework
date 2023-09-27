package io.tech1.framework.b2b.base.security.jwt.tests.random;

import io.tech1.framework.b2b.base.security.jwt.domain.db.UserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import lombok.experimental.UtilityClass;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomStringLetterOrNumbersOnly;
import static io.tech1.framework.domain.utilities.time.TimestampUtility.getCurrentTimestamp;
import static java.util.Collections.singletonList;

@UtilityClass
public class BaseSecurityJwtDbRandomUtility {

    // =================================================================================================================
    // InvitationCodes
    // =================================================================================================================
    public static ResponseInvitationCode getInvitationCode(Username owner) {
        return new ResponseInvitationCode(
                entity(InvitationCodeId.class),
                owner,
                singletonList("admin"),
                randomStringLetterOrNumbersOnly(40),
                null
        );
    }

    public static ResponseInvitationCode getInvitationCode(Username owner, Username invited) {
        return new ResponseInvitationCode(
                entity(InvitationCodeId.class),
                owner,
                singletonList("admin"),
                randomStringLetterOrNumbersOnly(40),
                invited
        );
    }

    // =================================================================================================================
    // UserSessions
    // =================================================================================================================
    public static UserSession session(Username owner, JwtAccessToken accessToken, JwtRefreshToken refreshToken) {
        return UserSession.ofPersisted(
                entity(UserSessionId.class),
                getCurrentTimestamp(),
                getCurrentTimestamp(),
                owner,
                accessToken,
                refreshToken,
                UserRequestMetadata.random(),
                false,
                false
        );
    }

    public static UserSession session(String owner, String accessToken, String refreshToken) {
        return session(
                Username.of(owner),
                JwtAccessToken.of(accessToken),
                JwtRefreshToken.of(refreshToken)
        );
    }

    public static UserSession session(Username owner, String accessToken) {
        return session(owner.identifier(), accessToken, entity(JwtRefreshToken.class).value());
    }

    public static UserSession session(String owner) {
        return session(Username.of(owner), entity(JwtAccessToken.class).value());
    }
}
