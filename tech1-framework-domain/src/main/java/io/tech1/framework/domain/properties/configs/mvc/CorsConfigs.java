package io.tech1.framework.domain.properties.configs.mvc;

import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class CorsConfigs extends AbstractPropertiesConfigs {
    @NonMandatoryProperty
    private String pathPattern;
    @NonMandatoryProperty
    private String[] allowedOrigins;
    @NonMandatoryProperty
    private String[] allowedMethods;
    @NonMandatoryProperty
    private String[] allowedHeaders;
    @NonMandatoryProperty
    private boolean allowCredentials;
    @NonMandatoryProperty
    private String[] exposedHeaders;

    // NOTE: test-purposes
    public static CorsConfigs of(
            String pathPattern,
            String[] allowedOrigins,
            String[] allowedMethods,
            String[] allowedHeaders,
            boolean allowCredentials,
            String[] exposedHeaders
    ) {
        var instance = new CorsConfigs();
        instance.pathPattern = pathPattern;
        instance.allowedOrigins = allowedOrigins;
        instance.allowedMethods = allowedMethods;
        instance.allowedHeaders = allowedHeaders;
        instance.allowCredentials = allowCredentials;
        instance.exposedHeaders = exposedHeaders;
        return instance;
    }
}
