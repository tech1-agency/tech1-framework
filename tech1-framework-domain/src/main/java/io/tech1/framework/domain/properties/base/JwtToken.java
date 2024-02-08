package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

import static io.tech1.framework.domain.asserts.Asserts.assertFalseOrThrow;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.util.Objects.nonNull;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class JwtToken extends AbstractPropertyConfigs {
    @MandatoryProperty
    private final TimeAmount expiration;
    @NonMandatoryProperty
    private String cookieKey;
    @NonMandatoryProperty
    private String headerKey;

    public static JwtToken testsHardcoded() {
        return new JwtToken(new TimeAmount(12L, HOURS), "cookieJWT", null);
    }

    public static JwtToken randomCookieBasedToken() {
        return new JwtToken(TimeAmount.random(), randomString(), null);
    }

    public static JwtToken randomHeaderBasedToken() {
        return new JwtToken(TimeAmount.random(), null, randomString());
    }

    @Override
    public void assertProperties(String parentName) {
        super.assertProperties(parentName);
        var bothProvided = nonNull(this.cookieKey) && nonNull(this.headerKey);
        assertFalseOrThrow(bothProvided, "Attribute `%s` requires only `cookieKey` or `headerKey` to be provided".formatted(parentName));
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
