package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.Data;

// Lombok (property-based)
@Data
public class JwtToken {
    @MandatoryProperty
    private String cookieKey;
    @MandatoryProperty
    private TimeAmount expiration;

    // NOTE: test-purposes
    public static JwtToken of(
            String cookieKey,
            TimeAmount expiration
    ) {
        var instance = new JwtToken();
        instance.cookieKey = cookieKey;
        instance.expiration = expiration;
        return instance;
    }
}
