package io.tech1.framework.incidents.domain.registration;

import io.tech1.framework.domain.base.Username;

public record IncidentRegistration1Failure(
        Username username,
        String invitationCode,
        Username invitationCodeOwner,
        String exception
) {
    public static IncidentRegistration1Failure of(
            Username username,
            String invitationCode,
            String exception
    ) {
        return new IncidentRegistration1Failure(
                username,
                invitationCode,
                Username.of("—"),
                exception
        );
    }
}
