package jbst.foundation.domain.properties.base;

import jbst.foundation.domain.constants.ZoneIdsConstants;
import jbst.foundation.domain.properties.annotations.MandatoryProperty;
import jbst.foundation.domain.properties.annotations.MandatoryToggleProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import static jbst.foundation.utilities.random.RandomUtility.randomBoolean;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class Cron extends AbstractTogglePropertyConfigs {
    @MandatoryProperty
    private final boolean enabled;
    @MandatoryToggleProperty
    private String expression;
    @MandatoryToggleProperty
    private String zoneId;

    public static Cron testsHardcoded() {
        return new Cron(true, "*/30 * * * * *", ZoneIdsConstants.UKRAINE.getId());
    }

    public static Cron enabled(String expression, String zoneId) {
        return new Cron(true, expression, zoneId);
    }

    public static Cron enabled() {
        return testsHardcoded();
    }

    public static Cron disabled() {
        return new Cron(false, null, null);
    }

    public static Cron random() {
        return randomBoolean() ? enabled() : disabled();
    }
}
