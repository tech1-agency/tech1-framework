package io.tech1.framework.incidents.domain.registration;

import io.tech1.framework.domain.base.Username;
import lombok.Data;

// Lombok
@Data(staticConstructor = "of")
public class Registration1Incident {
    private final Username username;
}
