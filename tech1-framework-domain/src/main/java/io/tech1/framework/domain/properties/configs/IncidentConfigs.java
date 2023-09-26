package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.RemoteServer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class IncidentConfigs extends AbstractPropertiesToggleConfigs {
    @MandatoryProperty
    private final boolean enabled;
    @MandatoryProperty
    private RemoteServer remoteServer;

    public static IncidentConfigs disabled() {
        return new IncidentConfigs(false, null);
    }
}
