package io.tech1.framework.b2b.base.security.jwt.domain.events;

import io.tech1.framework.domain.base.Username;

public record EventRegistration1Failure(
        Username username,
        String invitationCode,
        Username invitationCodeOwner,
        String exception
) {
    public static EventRegistration1Failure of(
            Username username,
            String exception,
            String invitationCode
    ) {
        return new EventRegistration1Failure(
                username,
                invitationCode,
                Username.of("—"),
                exception
        );
    }
}
