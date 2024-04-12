package io.tech1.framework.incidents.domain.system;

import io.tech1.framework.domain.base.Username;

public record IncidentSystemResetServerStarted(
        Username username
) {

    public static IncidentSystemResetServerStarted testsHardcoded() {
        return new IncidentSystemResetServerStarted(
                Username.testsHardcoded()
        );
    }
}
