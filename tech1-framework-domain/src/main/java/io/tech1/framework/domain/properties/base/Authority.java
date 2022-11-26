package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.Data;

// Lombok (property-based)
@Data
public class Authority {
    @MandatoryProperty
    private String value;

    // NOTE: test-purposes
    public static Authority of(
            String value
    ) {
        var instance = new Authority();
        instance.value = value;
        return instance;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
