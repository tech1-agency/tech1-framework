package io.tech1.framework.domain.properties.configs.security.jwt;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.TimeAmount;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class CookiesConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private String domain;
    @MandatoryProperty
    private TimeAmount jwtAccessTokenCookieCreationLatency;

    // NOTE: test-purposes
    public static CookiesConfigs of(
            String domain,
            TimeAmount jwtAccessTokenCookieCreationLatency
    ) {
        var instance = new CookiesConfigs();
        instance.domain = domain;
        instance.jwtAccessTokenCookieCreationLatency = jwtAccessTokenCookieCreationLatency;
        return instance;
    }
}
