package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.MandatoryToggleProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomBoolean;

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
        return new Cron(true, "*/30 * * * * *", "Europe/Kiev");
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
