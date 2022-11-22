package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import io.tech1.framework.domain.properties.configs.mvc.CorsConfigs;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class MvcConfigs extends AbstractPropertiesToggleConfigs {
    @MandatoryProperty
    private boolean enabled;
    @NonMandatoryProperty
    private String frameworkBasePathPrefix;
    @NonMandatoryProperty
    private CorsConfigs corsConfigs;

    public static MvcConfigs of(
            boolean enabled,
            String frameworkBasePathPrefix,
            CorsConfigs corsConfigs
    ) {
        var instance = new MvcConfigs();
        instance.enabled = enabled;
        instance.frameworkBasePathPrefix = frameworkBasePathPrefix;
        instance.corsConfigs = corsConfigs;
        return instance;
    }
}
