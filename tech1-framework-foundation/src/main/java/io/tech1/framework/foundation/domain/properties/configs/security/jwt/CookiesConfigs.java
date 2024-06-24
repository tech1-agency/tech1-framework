package io.tech1.framework.foundation.domain.properties.configs.security.jwt;

import io.tech1.framework.foundation.domain.constants.DomainConstants;
import io.tech1.framework.foundation.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.foundation.domain.properties.base.TimeAmount;
import io.tech1.framework.foundation.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import static io.tech1.framework.foundation.utilities.random.RandomUtility.randomString;
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

    public static CookiesConfigs random() {
        return new CookiesConfigs(randomString(), TimeAmount.random());
    }

    @Override
    public boolean isParentPropertiesNode() {
        return false;
    }
}
