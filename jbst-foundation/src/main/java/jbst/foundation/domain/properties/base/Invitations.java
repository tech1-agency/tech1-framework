package jbst.foundation.domain.properties.base;

import jbst.foundation.domain.properties.annotations.MandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import static jbst.foundation.utilities.random.RandomUtility.randomBoolean;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class Invitations extends AbstractTogglePropertyConfigs {
    @MandatoryProperty
    private final boolean enabled;

    public static Invitations hardcoded() {
        return new Invitations(true);
    }

    public static Invitations random() {
        return randomBoolean() ? enabled() : disabled();
    }

    public static Invitations enabled() {
        return hardcoded();
    }

    public static Invitations disabled() {
        return new Invitations(false);
    }
}
