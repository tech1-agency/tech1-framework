package jbst.iam.domain.dto.responses;

import jbst.foundation.domain.base.Username;
import jbst.iam.domain.identifiers.InvitationCodeId;

import java.util.Comparator;

import static java.util.Comparator.comparing;
import static java.util.Objects.nonNull;
import static jbst.foundation.utilities.random.RandomUtility.randomStringLetterOrNumbersOnly;

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
                nonNull(invited) ? invited.value() : "",
                nonNull(invited) ? "Used" : "Unused"
        );
    }

    public static ResponseInvitationCode random(Username owner) {
        return ResponseInvitationCode.of(
                InvitationCodeId.random(),
                owner,
                "admin",
                randomStringLetterOrNumbersOnly(40),
                null
        );
    }

    public static ResponseInvitationCode random(Username owner, Username invited) {
        return ResponseInvitationCode.of(
                InvitationCodeId.random(),
                owner,
                "admin",
                randomStringLetterOrNumbersOnly(40),
                invited
        );
    }
}
