package io.tech1.framework.b2b.base.security.jwt.domain.dto.requests;

import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomStringsAsList;

public record RequestNewInvitationCodeParams(
        @NotEmpty Set<String> authorities
) {

    public static RequestNewInvitationCodeParams random() {
        return new RequestNewInvitationCodeParams(new HashSet<>(randomStringsAsList(3)));
    }

    public static RequestNewInvitationCodeParams testsHardcoded() {
        return new RequestNewInvitationCodeParams(new HashSet<>(Set.of("invitationCode:read", "invitationCode:write")));
    }
}
