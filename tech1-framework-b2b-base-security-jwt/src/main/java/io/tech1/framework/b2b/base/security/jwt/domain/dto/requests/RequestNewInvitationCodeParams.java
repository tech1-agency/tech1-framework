package io.tech1.framework.b2b.base.security.jwt.domain.dto.requests;

import java.util.HashSet;
import java.util.Set;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomStringsAsList;

public record RequestNewInvitationCodeParams(
        Set<String> authorities
) {

    public static RequestNewInvitationCodeParams random() {
        return new RequestNewInvitationCodeParams(new HashSet<>(randomStringsAsList(3)));
    }
}
