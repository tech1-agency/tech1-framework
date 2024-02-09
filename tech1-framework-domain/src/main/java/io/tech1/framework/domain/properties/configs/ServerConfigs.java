package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class ServerConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private final String name;
    @NonMandatoryProperty
    private String webclientURL;

    public static ServerConfigs testsHardcoded() {
        return new ServerConfigs("tech1-spring-boot-server", "http://127.0.0.1:3000");
    }

    public static ServerConfigs random() {
        return new ServerConfigs(randomString(), randomString());
    }

    @Override
    public boolean isParentPropertiesNode() {
        return true;
    }
}
