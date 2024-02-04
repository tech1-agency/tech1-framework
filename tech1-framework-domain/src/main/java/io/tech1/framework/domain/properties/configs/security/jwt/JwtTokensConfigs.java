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
import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.constants.FrameworkLogsConstants.FRAMEWORK_PROPERTIES_PREFIX;
import static io.tech1.framework.domain.constants.FrameworkLogsConstants.LINE_SEPARATOR_INTERPUNCT;
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
                new JwtToken(new TimeAmount(30L, SECONDS), "ajwt", "T-AJWT"),
                new JwtToken(new TimeAmount(12L, HOURS), "rjwt", "T-RJWT")
        );
    }

    @Override
    public void assertProperties() {
        LOGGER.info(LINE_SEPARATOR_INTERPUNCT);
        super.assertProperties();
        if (this.storageMethod.isCookies()) {
            assertNonNullOrThrow(this.accessToken.getCookieKey(), "Please specify `jwtTokensConfigs.accessToken.cookieKey` attribute");
            assertNonNullOrThrow(this.refreshToken.getCookieKey(), "Please specify `jwtTokensConfigs.refreshToken.cookieKey` attribute");
            assertFalseOrThrow(
                    this.accessToken.getCookieKey().equals(this.refreshToken.getCookieKey()),
                    "Please make sure `jwtTokensConfigs.accessToken.cookieKey` and `jwtTokensConfigs.refreshToken.cookieKey` are different"
            );
        }
        if (this.storageMethod.isHeaders()) {
            assertNonNullOrThrow(this.accessToken.getHeaderKey(), "Please specify `jwtTokensConfigs.accessToken.headerKey` attribute");
            assertNonNullOrThrow(this.refreshToken.getHeaderKey(), "Please specify `jwtTokensConfigs.refreshToken.headerKey` attribute");
            assertFalseOrThrow(
                    this.accessToken.getHeaderKey().equals(this.refreshToken.getHeaderKey()),
                    "Please make sure `jwtTokensConfigs.accessToken.headerKey` and `jwtTokensConfigs.refreshToken.headerKey` are different"
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
