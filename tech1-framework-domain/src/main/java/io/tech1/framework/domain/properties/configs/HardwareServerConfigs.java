package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class HardwareServerConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private final String baseURL;

    public static HardwareServerConfigs testsHardcoded() {
        return new HardwareServerConfigs(
                "http://localhost:8484"
        );
    }
}
