package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.Data;

// Lombok (property-based)
@Data
public class Checkbox implements AbstractToggleProperty {
    @MandatoryProperty
    private boolean enabled;

    // NOTE: test-purposes
    private static Checkbox of(
            boolean enabled
    ) {
        var instance = new Checkbox();
        instance.enabled = enabled;
        return instance;
    }

    // NOTE: test-purposes
    public static Checkbox enabled() {
        return of(
                true
        );
    }

    // NOTE: test-purposes
    public static Checkbox disabled() {
        return of(
                false
        );
    }
}
