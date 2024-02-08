package io.tech1.framework.domain.properties.base;

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
public class SpringLogging extends AbstractPropertyConfigs {
    @MandatoryProperty
    private final String config;

    public static SpringLogging testsHardcoded() {
        return new SpringLogging("logback-test.xml");
    }

    public static SpringLogging random() {
        return new SpringLogging(randomString());
    }
}
