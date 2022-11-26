package io.tech1.framework.domain.properties.configs.security.jwt;

import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class LoggingConfigs extends AbstractPropertiesConfigs {
    private boolean advancedRequestLoggingEnabled;

    // NOTE: test-purposes
    public static LoggingConfigs of(
            boolean advancedRequestLoggingEnabled
    ) {
        var instance = new LoggingConfigs();
        instance.advancedRequestLoggingEnabled = advancedRequestLoggingEnabled;
        return instance;
    }
}
