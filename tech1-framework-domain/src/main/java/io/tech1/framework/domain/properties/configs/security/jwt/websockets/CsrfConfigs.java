package io.tech1.framework.domain.properties.configs.security.jwt.websockets;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class CsrfConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private final String cookieName;
    @MandatoryProperty
    private final String headerName;
    @MandatoryProperty
    private final String parameterName;

    public static CsrfConfigs testsHardcoded() {
        return new CsrfConfigs("csrf-cookie", "csrf-header", "csrf-parameter");
    }
}
