package io.tech1.framework.b2b.base.security.jwt.tests.random;

import io.tech1.framework.b2b.base.security.jwt.domain.db.UserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.foundation.domain.base.Username;
import io.tech1.framework.foundation.domain.http.requests.UserRequestMetadata;
import lombok.experimental.UtilityClass;

import static io.tech1.framework.foundation.utilities.random.EntityUtility.entity;
import static io.tech1.framework.foundation.utilities.random.RandomUtility.randomStringLetterOrNumbersOnly;
import static io.tech1.framework.foundation.utilities.time.TimestampUtility.getCurrentTimestamp;

@UtilityClass
public class BaseSecurityJwtDbRandomUtility {

    // =================================================================================================================
    // InvitationCodes
    // =================================================================================================================
    public static ResponseInvitationCode getInvitationCode(Username owner) {
        return ResponseInvitationCode.of(
                InvitationCodeId.random(),
                owner,
                "admin",
                randomStringLetterOrNumbersOnly(40),
                null
        );
    }

    public static ResponseInvitationCode getInvitationCode(Username owner, Username invited) {
        return ResponseInvitationCode.of(
                InvitationCodeId.random(),
                owner,
                "admin",
                randomStringLetterOrNumbersOnly(40),
                invited
        );
    }

    // =================================================================================================================
    // UserSessions
    // =================================================================================================================
    public static UserSession session(Username owner, JwtAccessToken accessToken, JwtRefreshToken refreshToken) {
        return UserSession.ofPersisted(
                UserSessionId.random(),
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
        return session(owner.value(), accessToken, entity(JwtRefreshToken.class).value());
    }

    public static UserSession session(String owner) {
        return session(Username.of(owner), entity(JwtAccessToken.class).value());
    }
}
