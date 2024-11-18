package jbst.iam.domain.events;

import jbst.foundation.domain.base.Username;

public record EventRegistration1Failure(
        Username username,
        String invitationCode,
        Username invitationCodeOwner,
        String exception
) {
    public static EventRegistration1Failure of(
            Username username,
            String invitationCode,
            String exception
    ) {
        return new EventRegistration1Failure(
                username,
                invitationCode,
                Username.of("—"),
                exception
        );
    }
}
