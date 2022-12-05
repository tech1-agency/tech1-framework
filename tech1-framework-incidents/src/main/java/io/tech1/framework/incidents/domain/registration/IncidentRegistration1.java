package io.tech1.framework.incidents.domain.registration;

import io.tech1.framework.domain.base.Username;
import lombok.Data;

// Lombok
@Data(staticConstructor = "of")
public class IncidentRegistration1 {
    private final Username username;
}
