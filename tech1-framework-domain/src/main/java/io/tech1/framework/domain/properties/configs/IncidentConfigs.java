package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.RemoteServer;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class IncidentConfigs extends AbstractPropertiesToggleConfigs {
    @MandatoryProperty
    private boolean enabled;
    @MandatoryProperty
    private RemoteServer remoteServer;

    // NOTE: test-purposes
    public static IncidentConfigs of(
            boolean enabled,
            RemoteServer remoteServer
    ) {
        var instance = new IncidentConfigs();
        instance.enabled = enabled;
        instance.remoteServer = remoteServer;
        return instance;
    }

    // NOTE: test-purposes
    public static IncidentConfigs disabled() {
        var instance = new IncidentConfigs();
        instance.enabled = false;
        return instance;
    }
}
