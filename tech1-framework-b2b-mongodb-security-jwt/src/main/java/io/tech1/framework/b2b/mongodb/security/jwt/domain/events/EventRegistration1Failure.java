package io.tech1.framework.b2b.mongodb.security.jwt.domain.events;

import io.tech1.framework.domain.base.Username;
import lombok.Data;

// Lombok
@Data(staticConstructor = "of")
public class EventRegistration1Failure {
    private final Username username;
    private final String exception;
    private final String invitationCode;
}
