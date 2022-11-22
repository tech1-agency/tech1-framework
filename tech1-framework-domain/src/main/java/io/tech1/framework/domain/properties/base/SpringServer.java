package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.Data;

// Lombok (property-based)
@Data
public class SpringServer {
    @MandatoryProperty
    private int port;

    // NOTE: test-purposes
    public static SpringServer of(
            int port
    ) {
        var instance = new SpringServer();
        instance.port = port;
        return instance;
    }
}
