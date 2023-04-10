package io.tech1.framework.incidents.domain.registration;

import io.tech1.framework.domain.base.Username;
import lombok.Data;

// Lombok
@Data
public class IncidentRegistration1Failure {
    private final Username username;
    private final String invitationCode;
    private final Username invitationCodeOwner;
    private final String exception;

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
