package io.tech1.framework.domain.properties.configs.security.jwt;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.JwtToken;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.Data;
import lombok.EqualsAndHashCode;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class JwtTokensConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private String secretKey;
    @MandatoryProperty
    private JwtToken accessToken;
    @MandatoryProperty
    private JwtToken refreshToken;

    // NOTE: test-purposes
    public static JwtTokensConfigs of(
            String secretKey,
            JwtToken accessToken,
            JwtToken refreshToken
    ) {
        var instance = new JwtTokensConfigs();
        instance.secretKey = secretKey;
        instance.accessToken = accessToken;
        instance.refreshToken = refreshToken;
        return instance;
    }
}
