package tech1.framework.foundation.domain.properties.configs.mvc;

import tech1.framework.foundation.domain.properties.annotations.NonMandatoryProperty;
import tech1.framework.foundation.domain.properties.base.AbstractPropertyConfigs;
import tech1.framework.foundation.utilities.random.RandomUtility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Set;

import static tech1.framework.foundation.utilities.random.RandomUtility.randomBoolean;
import static tech1.framework.foundation.utilities.random.RandomUtility.randomString;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class CorsConfigs extends AbstractPropertyConfigs {
    @NonMandatoryProperty
    private String pathPattern;
    @NonMandatoryProperty
    private String[] allowedOrigins;
    @NonMandatoryProperty
    private String[] allowedMethods;
    @NonMandatoryProperty
    private String[] allowedHeaders;
    @NonMandatoryProperty
    private boolean allowCredentials;
    @NonMandatoryProperty
    private String[] exposedHeaders;

    public static CorsConfigs testsHardcoded() {
        return new CorsConfigs(
                "/api/**",
                new String[] { "http://localhost:8080", "http://localhost:8081" },
                new String[] { "GET", "POST" },
                new String[] { "Access-Control-Allow-Origin" },
                true,
                null
        );
    }

    public static CorsConfigs random() {
        return new CorsConfigs(
                randomString(),
                new String[] { randomString(), randomString() },
                new String[] { RandomUtility.randomElement(Set.of("GET", "POST", "PUT", "DELETE")) },
                new String[] { randomString() },
                randomBoolean(),
                new String[] { randomString() }
        );
    }
}
