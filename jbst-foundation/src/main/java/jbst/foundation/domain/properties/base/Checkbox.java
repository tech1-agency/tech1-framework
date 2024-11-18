package jbst.foundation.domain.properties.base;

import jbst.foundation.domain.properties.annotations.MandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import static jbst.foundation.utilities.random.RandomUtility.randomBoolean;

@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class Checkbox extends AbstractTogglePropertyConfigs {
    @MandatoryProperty
    private final boolean enabled;

    public static Checkbox hardcoded() {
        return new Checkbox(true);
    }

    public static Checkbox random() {
        return randomBoolean() ? enabled() : disabled();
    }

    public static Checkbox enabled() {
        return hardcoded();
    }

    public static Checkbox disabled() {
        return new Checkbox(false);
    }
}
