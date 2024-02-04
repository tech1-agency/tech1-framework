package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
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
public class JwtToken extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private final TimeAmount expiration;
    @NonMandatoryProperty
    private String cookieKey;
    @NonMandatoryProperty
    private String headerKey;

    public JwtToken(TimeAmount expiration, String cookieKey) {
        this.expiration = expiration;
        this.cookieKey = cookieKey;
    }
}
