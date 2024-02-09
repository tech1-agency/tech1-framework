package io.tech1.framework.domain.properties.configs.security.jwt;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.base.JwtToken;
import io.tech1.framework.domain.properties.base.JwtTokenStorageMethod;
import io.tech1.framework.domain.properties.base.TimeAmount;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConstructorBinding;

import static io.tech1.framework.domain.asserts.Asserts.assertFalseOrThrow;
import static io.tech1.framework.domain.constants.FrameworkLogsConstants.FRAMEWORK_PROPERTIES_PREFIX;
import static io.tech1.framework.domain.constants.FrameworkLogsConstants.LINE_SEPARATOR_INTERPUNCT;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomEnum;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.SECONDS;

@Slf4j
// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class JwtTokensConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private final String secretKey;
    @MandatoryProperty
    private final JwtTokenStorageMethod storageMethod;
    @MandatoryProperty
    private final JwtToken accessToken;
    @MandatoryProperty
    private final JwtToken refreshToken;

    public static JwtTokensConfigs testsHardcoded() {
        return new JwtTokensConfigs(
                "TECH1",
                JwtTokenStorageMethod.COOKIES,
                new JwtToken(new TimeAmount(30L, SECONDS), "ajwt", null),
                new JwtToken(new TimeAmount(12L, HOURS), "rjwt", null)
        );
    }

    public static JwtTokensConfigs random() {
        return new JwtTokensConfigs(
                randomString(),
                randomEnum(JwtTokenStorageMethod.class),
                JwtToken.random(),
                JwtToken.random()
        );
    }

    @Override
    public boolean isParentPropertiesNode() {
        return false;
    }

    @Override
    public void assertProperties(String propertyName) {
        LOGGER.info(LINE_SEPARATOR_INTERPUNCT);
        super.assertProperties(propertyName);
        if (this.storageMethod.isCookies()) {
            assertFalseOrThrow(
                    this.accessToken.getCookieKey().equals(this.refreshToken.getCookieKey()),
                    "Please make sure `%s.accessToken.cookieKey` and `%s.refreshToken.cookieKey` are different".formatted(propertyName, propertyName)
            );
        }
        if (this.storageMethod.isHeaders()) {
            assertFalseOrThrow(
                    this.accessToken.getHeaderKey().equals(this.refreshToken.getHeaderKey()),
                    "Please make sure `%s.accessToken.headerKey` and `%s.refreshToken.headerKey` are different".formatted(propertyName, propertyName)
            );
        }
        LOGGER.info(
                "{}, JWT tokens are stored using {} keys: accessTokenKey = \"{}\", refreshTokenKey \"{}\"",
                FRAMEWORK_PROPERTIES_PREFIX,
                this.storageMethod,
                this.accessToken.getKey(this.storageMethod),
                this.refreshToken.getKey(this.storageMethod)
        );
        LOGGER.info(LINE_SEPARATOR_INTERPUNCT);
    }
}
