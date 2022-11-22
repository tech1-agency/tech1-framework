package io.tech1.framework.domain.properties.base;

import lombok.Data;

// Lombok (property-based)
@Data
public class Authority {
    private String value;

    // NOTE: test-purposes
    public static Authority of(
            String value
    ) {
        var instance = new Authority();
        instance.value = value;
        return instance;
    }
}
