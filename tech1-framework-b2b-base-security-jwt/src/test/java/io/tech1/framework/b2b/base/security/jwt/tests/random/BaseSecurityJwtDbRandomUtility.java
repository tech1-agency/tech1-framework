package io.tech1.framework.b2b.base.security.jwt.tests.random;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.domain.base.Username;
import lombok.experimental.UtilityClass;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomStringLetterOrNumbersOnly;
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
}
