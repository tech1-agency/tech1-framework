package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class EmailConfigs extends AbstractPropertiesToggleConfigs {
    @MandatoryProperty
    private boolean enabled;
    @NonMandatoryProperty
    private String host;
    @NonMandatoryProperty
    private String port;
    @NonMandatoryProperty
    private String username;
    @NonMandatoryProperty
    private String from;
    @NonMandatoryProperty
    private String password;
    @NonMandatoryProperty
    private String[] to;

    // NOTE: test-purposes
    public static EmailConfigs of(
            boolean enabled,
            String host,
            String port,
            String username,
            String from,
            String password,
            String[] to
    ) {
        var instance = new EmailConfigs();
        instance.enabled = enabled;
        instance.host = host;
        instance.port = port;
        instance.username = username;
        instance.from = from;
        instance.password = password;
        instance.to = to;
        return instance;
    }
}
