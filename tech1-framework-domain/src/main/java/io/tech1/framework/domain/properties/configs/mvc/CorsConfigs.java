package io.tech1.framework.domain.properties.configs.mvc;

import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
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

    public static CorsConfigs testsHardcoded() {
        return new CorsConfigs(
                "/api/**",
                new String[] { "http://localhost:8080", "http://localhost:8081" },
                new String[] { "GET", "POST" },
                new String[] { "Access-Control-Allow-Origin" },
                true,
                null
        );
    }
}
