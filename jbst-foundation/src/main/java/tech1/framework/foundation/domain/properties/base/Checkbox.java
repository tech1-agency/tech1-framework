package tech1.framework.foundation.domain.properties.base;

import tech1.framework.foundation.domain.properties.annotations.MandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import static tech1.framework.foundation.utilities.random.RandomUtility.randomBoolean;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class Checkbox extends AbstractTogglePropertyConfigs {
    @MandatoryProperty
    private final boolean enabled;

    public static Checkbox testsHardcoded() {
        return new Checkbox(true);
    }

    public static Checkbox random() {
        return randomBoolean() ? enabled() : disabled();
    }

    public static Checkbox enabled() {
        return testsHardcoded();
    }

    public static Checkbox disabled() {
        return new Checkbox(false);
    }
}
