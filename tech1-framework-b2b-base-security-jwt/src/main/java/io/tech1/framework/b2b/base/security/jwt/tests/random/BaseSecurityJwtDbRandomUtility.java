package io.tech1.framework.b2b.base.security.jwt.tests.random;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbUserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.base.Username;
import lombok.experimental.UtilityClass;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomStringLetterOrNumbersOnly;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomUserRequestMetadata;
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
    public static AnyDbUserSession session(String owner, String accessToken, String refreshToken) {
        return AnyDbUserSession.ofPersisted(
                entity(UserSessionId.class),
                Username.of(owner),
                JwtAccessToken.of(accessToken),
                JwtRefreshToken.of(refreshToken),
                randomUserRequestMetadata()
        );
    }

    public static AnyDbUserSession session(Username owner, String accessToken) {
        return session(owner.identifier(), accessToken, entity(JwtRefreshToken.class).value());
    }

    public static AnyDbUserSession session(String owner) {
        return session(Username.of(owner), entity(JwtAccessToken.class).value());
    }
}
