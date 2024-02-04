package io.tech1.framework.domain.properties.configs.security.jwt;

import io.tech1.framework.domain.constants.DomainConstants;
import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.TimeAmount;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

import static java.time.temporal.ChronoUnit.SECONDS;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class CookiesConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private final String domain;
    @MandatoryProperty
    private final TimeAmount jwtAccessTokenCookieCreationLatency;

    public static CookiesConfigs testsHardcoded() {
        return new CookiesConfigs(DomainConstants.TECH1, new TimeAmount(5L, SECONDS));
    }
}
