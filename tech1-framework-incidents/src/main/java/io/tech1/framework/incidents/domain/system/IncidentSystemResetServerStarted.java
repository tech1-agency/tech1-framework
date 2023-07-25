package io.tech1.framework.incidents.domain.system;

import io.tech1.framework.domain.base.Username;

public record IncidentSystemResetServerStarted(
        Username username
) {
}
