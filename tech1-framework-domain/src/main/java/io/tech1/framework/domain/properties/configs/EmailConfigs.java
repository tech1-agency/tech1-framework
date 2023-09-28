package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class EmailConfigs extends AbstractPropertiesToggleConfigs {
    @MandatoryProperty
    private final boolean enabled;
    @NonMandatoryProperty
    private String host;
    @NonMandatoryProperty
    private int port;
    @NonMandatoryProperty
    private String from;
    @NonMandatoryProperty
    private String username;
    @NonMandatoryProperty
    private String password;
    @NonMandatoryProperty
    private String[] to;

    public static EmailConfigs disabled() {
        return new EmailConfigs(false, null, 0, null, null, null, null);
    }

    public static EmailConfigs enabled(String from) {
        return new EmailConfigs(true, null, 0, from, null, null, null);
    }
}
