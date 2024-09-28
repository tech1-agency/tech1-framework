package io.tech1.framework.iam.domain.events;

import tech1.framework.foundation.domain.base.Username;

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
