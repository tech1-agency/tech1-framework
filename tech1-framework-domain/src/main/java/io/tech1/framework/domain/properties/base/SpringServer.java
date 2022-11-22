package io.tech1.framework.domain.properties.base;

import lombok.Data;

// Lombok (property-based)
@Data
public class SpringServer {
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
