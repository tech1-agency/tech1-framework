package jbst.foundation.domain.properties.configs;

import jbst.foundation.domain.properties.annotations.MandatoryProperty;
import jbst.foundation.domain.properties.annotations.MandatoryToggleProperty;
import jbst.foundation.domain.properties.base.RemoteServer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import static jbst.foundation.utilities.random.RandomUtility.randomBoolean;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class IncidentConfigs extends AbstractTogglePropertiesConfigs {
    @MandatoryProperty
    private final boolean enabled;
    @MandatoryToggleProperty
    private RemoteServer remoteServer;

    public static IncidentConfigs hardcoded() {
        return new IncidentConfigs(true, RemoteServer.hardcoded());
    }

    public static IncidentConfigs random() {
        return new IncidentConfigs(randomBoolean(), RemoteServer.random());
    }

    public static IncidentConfigs enabled() {
        return hardcoded();
    }

    public static IncidentConfigs disabled() {
        return new IncidentConfigs(false, null);
    }

    @Override
    public boolean isParentPropertiesNode() {
        return true;
    }
}
