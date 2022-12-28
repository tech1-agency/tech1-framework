package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.Data;

// Lombok (property-based)
@Data
public class Cron implements AbstractToggleProperty {
    @MandatoryProperty
    private boolean enabled;
    @MandatoryProperty
    private String expression;
    @MandatoryProperty
    private String zoneId;

    // NOTE: test-purposes
    public static Cron of(
            boolean enabled,
            String expression,
            String zoneId
    ) {
        var instance = new Cron();
        instance.enabled = enabled;
        instance.expression = expression;
        instance.zoneId = zoneId;
        return instance;
    }

    // NOTE: test-purposes
    public static Cron enabled() {
        return Cron.of(
                true,
                null,
                null
        );
    }


    // NOTE: test-purposes
    public static Cron disabled() {
        return Cron.of(
                false,
                null,
                null
        );
    }
}
