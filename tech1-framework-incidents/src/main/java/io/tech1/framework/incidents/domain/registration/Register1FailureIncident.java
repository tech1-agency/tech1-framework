package io.tech1.framework.incidents.domain.registration;

import io.tech1.framework.domain.base.Username;
import lombok.Data;

// Lombok
@Data(staticConstructor = "of")
public class Register1FailureIncident {
    private final Username username;
    private final String exception;
    private final String invitationCode;
}
