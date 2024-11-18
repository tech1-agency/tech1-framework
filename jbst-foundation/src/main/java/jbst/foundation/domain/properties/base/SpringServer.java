package jbst.foundation.domain.properties.base;

import jbst.foundation.domain.properties.annotations.MandatoryProperty;
import jbst.foundation.utilities.random.RandomUtility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class SpringServer extends AbstractPropertyConfigs {
    @MandatoryProperty
    private final Integer port;

    public static SpringServer hardcoded() {
        return new SpringServer(8080);
    }

    public static SpringServer random() {
        return new SpringServer(RandomUtility.randomIntegerGreaterThanZeroByBounds(8000, 8100));
    }
}
