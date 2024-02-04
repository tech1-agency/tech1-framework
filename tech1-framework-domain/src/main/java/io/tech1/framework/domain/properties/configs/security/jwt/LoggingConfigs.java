package io.tech1.framework.domain.properties.configs.security.jwt;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.configs.AbstractPropertiesConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.EnumMap;
import java.util.Map;

import static io.tech1.framework.domain.properties.base.SecurityJwtIncidentType.*;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class LoggingConfigs extends AbstractPropertiesConfigs {
    @MandatoryProperty
    private final boolean advancedRequestLoggingEnabled;

    public static LoggingConfigs testsHardcoded() {
        return LoggingConfigs.enabled();
    }

    public static LoggingConfigs enabled() {
        return new LoggingConfigs(true);
    }

    public static LoggingConfigs disabled() {
        return new LoggingConfigs(false);
    }
}
