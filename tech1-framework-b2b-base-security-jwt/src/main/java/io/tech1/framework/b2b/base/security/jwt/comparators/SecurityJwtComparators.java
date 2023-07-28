package io.tech1.framework.b2b.base.security.jwt.comparators;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSession2;
import lombok.experimental.UtilityClass;

import java.util.Comparator;

import static java.util.Comparator.comparing;

@UtilityClass
public class SecurityJwtComparators {
    public static final Comparator<ResponseInvitationCode> INVITATION_CODE_1 =
            comparing((ResponseInvitationCode code) -> code.owner().identifier())
                    .thenComparing(ResponseInvitationCode::value);

    public static final Comparator<ResponseUserSession2> SESSIONS_2 =
            comparing((ResponseUserSession2 session) -> session.who().identifier())
                    .thenComparing(ResponseUserSession2::where);
}
