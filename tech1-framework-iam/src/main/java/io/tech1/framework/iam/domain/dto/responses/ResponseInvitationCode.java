package io.tech1.framework.iam.domain.dto.responses;

import io.tech1.framework.iam.domain.identifiers.InvitationCodeId;
import io.tech1.framework.foundation.domain.base.Username;

import java.util.Comparator;

import static io.tech1.framework.foundation.domain.constants.StringConstants.EMPTY;
import static java.util.Comparator.comparing;
import static java.util.Objects.nonNull;

public record ResponseInvitationCode(
        InvitationCodeId id,
        Username owner,
        String authorities,
        String value,
        String invited,
        String usage
) {
    public static final Comparator<ResponseInvitationCode> INVITATION_CODE = comparing(ResponseInvitationCode::usage).thenComparing(ResponseInvitationCode::value);

    public static ResponseInvitationCode of(
            InvitationCodeId id,
            Username owner,
            String authorities,
            String value,
            Username invited
    ) {
        return new ResponseInvitationCode(
                id,
                owner,
                authorities,
                value,
                nonNull(invited) ? invited.value() : EMPTY,
                nonNull(invited) ? "Used" : "Unused"
        );
    }
}
