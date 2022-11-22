package io.tech1.framework.domain.properties.configs.security.jwt.websockets;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class CsrfConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private String cookieName;
    @MandatoryProperty
    private String headerName;
    @MandatoryProperty
    private String parameterName;

    // NOTE: test-purposes
    public static CsrfConfigs of(
            String cookieName,
            String headerName,
            String parameterName
    ) {
        var instance = new CsrfConfigs();
        instance.cookieName = cookieName;
        instance.headerName = headerName;
        instance.parameterName = parameterName;
        return instance;
    }
}
