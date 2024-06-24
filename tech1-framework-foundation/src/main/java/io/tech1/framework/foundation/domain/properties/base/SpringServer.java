package io.tech1.framework.foundation.domain.properties.base;

import io.tech1.framework.foundation.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.foundation.utilities.random.RandomUtility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class SpringServer extends AbstractPropertyConfigs {
    @MandatoryProperty
    private final Integer port;

    public static SpringServer testsHardcoded() {
        return new SpringServer(8080);
    }

    public static SpringServer random() {
        return new SpringServer(RandomUtility.randomIntegerGreaterThanZeroByBounds(8000, 8100));
    }
}
