package tech1.framework.foundation.domain.properties.configs.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import tech1.framework.foundation.domain.constants.JbsConstants;
import tech1.framework.foundation.domain.properties.annotations.MandatoryProperty;
import tech1.framework.foundation.domain.properties.base.TimeAmount;
import tech1.framework.foundation.domain.properties.configs.AbstractPropertiesConfigs;

import static java.time.temporal.ChronoUnit.SECONDS;
import static tech1.framework.foundation.utilities.random.RandomUtility.randomString;

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
        return new CookiesConfigs(JbsConstants.Domains.HARDCODED, new TimeAmount(5L, SECONDS));
    }

    public static CookiesConfigs random() {
        return new CookiesConfigs(randomString(), TimeAmount.random());
    }

    @Override
    public boolean isParentPropertiesNode() {
        return false;
    }
}
