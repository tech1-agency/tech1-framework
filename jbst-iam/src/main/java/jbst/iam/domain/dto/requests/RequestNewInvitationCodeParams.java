package jbst.iam.domain.dto.requests;

import jakarta.validation.constraints.NotEmpty;

import java.util.HashSet;
import java.util.Set;

import static jbst.foundation.utilities.random.RandomUtility.randomStringsAsList;

public record RequestNewInvitationCodeParams(
        @NotEmpty Set<String> authorities
) {

    public static RequestNewInvitationCodeParams hardcoded() {
        return new RequestNewInvitationCodeParams(new HashSet<>(Set.of("invitations:read", "invitations:write")));
    }

    public static RequestNewInvitationCodeParams random() {
        return new RequestNewInvitationCodeParams(new HashSet<>(randomStringsAsList(3)));
    }
}
