package io.tech1.framework.b2b.mongodb.security.jwt.comparators;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseInvitationCode1;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseUserSession2;
import lombok.experimental.UtilityClass;

import java.util.Comparator;

import static java.util.Comparator.comparing;

@UtilityClass
public class SecurityJwtComparators {
    public static final Comparator<ResponseInvitationCode1> INVITATION_CODE_1 =
            comparing((ResponseInvitationCode1 code) -> code.getOwner().identifier())
                    .thenComparing(ResponseInvitationCode1::getValue);

    public static final Comparator<ResponseUserSession2> SESSIONS_2 =
            comparing((ResponseUserSession2 session) -> session.getWho().identifier())
                    .thenComparing(ResponseUserSession2::getWhere);
}
