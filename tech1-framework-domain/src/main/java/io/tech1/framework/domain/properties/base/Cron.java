package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConstructorBinding;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomBoolean;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
public class Cron implements AbstractToggleProperty {
    @MandatoryProperty
    private final boolean enabled;
    @MandatoryProperty
    private String expression;
    @MandatoryProperty
    private String zoneId;

    public static Cron enabled(
            String expression,
            String zoneId
    ) {
        return new Cron(
                true,
                expression,
                zoneId
        );
    }

    public static Cron enabled() {
        return new Cron(
                true,
                "*/30 * * * * *",
                "Europe/Kiev"
        );
    }

    public static Cron disabled() {
        return new Cron(
                false,
                null,
                null
        );
    }

    public static Cron random() {
        return randomBoolean() ? enabled() : disabled();
    }
}
