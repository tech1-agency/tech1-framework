package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class AsyncConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private final String threadNamePrefix;

    public static AsyncConfigs testsHardcoded() {
        return new AsyncConfigs("tech1-async");
    }

    public static AsyncConfigs random() {
        return new AsyncConfigs(randomString());
    }
}
