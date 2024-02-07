package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import io.tech1.framework.domain.properties.base.RemoteServer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConstructorBinding;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@RequiredArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class IncidentConfigs extends AbstractPropertiesToggleConfigs {
    @MandatoryProperty
    private final boolean enabled;
    @NonMandatoryProperty
    private RemoteServer remoteServer;

    public static IncidentConfigs testsHardcoded() {
        return new IncidentConfigs(true, RemoteServer.testsHardcoded());
    }

    public static IncidentConfigs disabled() {
        return new IncidentConfigs(false);
    }
}
