package io.tech1.framework.b2b.mongodb.security.jwt.domain.events;

import io.tech1.framework.domain.base.Username;
import lombok.Data;

// Lombok
@Data
public class EventRegistration1Failure {
    private final Username username;
    private final String invitationCode;
    private final Username invitationCodeOwner;
    private final String exception;

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
