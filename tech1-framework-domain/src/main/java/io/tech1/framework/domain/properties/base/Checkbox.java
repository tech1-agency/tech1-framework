package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.Data;

// Lombok (property-based)
@Data
public class Checkbox implements AbstractToggleProperty {
    @MandatoryProperty
    private boolean enabled;

    // NOTE: test-purposes
    public static Checkbox enabled() {
        var instance = new Checkbox();
        instance.enabled = true;
        return instance;
    }

    // NOTE: test-purposes
    public static Checkbox disabled() {
        var instance = new Checkbox();
        instance.enabled = false;
        return instance;
    }
}
