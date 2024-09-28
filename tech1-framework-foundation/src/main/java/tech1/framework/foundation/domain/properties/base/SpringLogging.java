package tech1.framework.foundation.domain.properties.base;

import tech1.framework.foundation.domain.properties.annotations.MandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import static tech1.framework.foundation.utilities.random.RandomUtility.randomString;

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
