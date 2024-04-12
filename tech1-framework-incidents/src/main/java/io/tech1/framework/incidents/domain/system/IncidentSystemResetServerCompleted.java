package io.tech1.framework.incidents.domain.system;

import io.tech1.framework.domain.base.Username;

public record IncidentSystemResetServerCompleted(
        Username username
) {

    public static IncidentSystemResetServerCompleted testsHardcoded() {
        return new IncidentSystemResetServerCompleted(
                Username.testsHardcoded()
        );
    }
}
