package io.tech1.framework.incidents.domain.system;

import io.tech1.framework.domain.base.Username;
import lombok.Data;

// Lombok
@Data
public class IncidentSystemResetServerStarted {
    private final Username username;
}
