package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import io.tech1.framework.domain.properties.configs.security.jwt.JwtTokensConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.SECONDS;

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

    public static JwtToken testsHardcoded() {
        return new JwtToken(new TimeAmount(12L, HOURS), "cookieJWT", "T-HEADER-JWT");
    }

    public String getKey(JwtTokenStorageMethod method) {
        if (method.isCookies()) {
            return this.cookieKey;
        }
        if (method.isHeaders()) {
            return this.headerKey;
        }
        throw new IllegalArgumentException();
    }
}
