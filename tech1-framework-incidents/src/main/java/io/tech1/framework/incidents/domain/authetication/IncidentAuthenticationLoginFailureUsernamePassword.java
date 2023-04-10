package io.tech1.framework.incidents.domain.authetication;

import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import lombok.Data;

// Lombok
@Data
public class IncidentAuthenticationLoginFailureUsernamePassword {
    private final Username username;
    private final Password password;
}
