package io.tech1.framework.domain.properties.base;

import lombok.Data;

// Lombok (property-based)
@Data
public class SpringLogging {
    private String config;

    // NOTE: test-purposes
    public static SpringLogging of(
            String config
    ) {
        var instance = new SpringLogging();
        instance.config = config;
        return instance;
    }
}
