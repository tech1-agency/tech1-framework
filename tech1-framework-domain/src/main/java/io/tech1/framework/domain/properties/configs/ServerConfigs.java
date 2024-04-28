package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.base.ServerName;
import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomBoolean;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class ServerConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private final ServerName name;
    @MandatoryProperty
    private final Boolean springdocEnabled;
    @NonMandatoryProperty
    private String webclientURL;

    public static ServerConfigs testsHardcoded() {
        return new ServerConfigs(ServerName.testsHardcoded(), true, "http://127.0.0.1:3000");
    }

    public static ServerConfigs random() {
        return new ServerConfigs(ServerName.random(), randomBoolean(), randomString());
    }

    @Override
    public boolean isParentPropertiesNode() {
        return true;
    }

    public boolean isSpringdocEnabled() {
        return this.springdocEnabled;
    }
}
