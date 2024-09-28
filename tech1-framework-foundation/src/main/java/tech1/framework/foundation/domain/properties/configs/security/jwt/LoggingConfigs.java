package tech1.framework.foundation.domain.properties.configs.security.jwt;

import tech1.framework.foundation.domain.properties.annotations.MandatoryProperty;
import tech1.framework.foundation.domain.properties.base.AbstractPropertyConfigs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import static tech1.framework.foundation.utilities.random.RandomUtility.randomBoolean;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class LoggingConfigs extends AbstractPropertyConfigs {
    @MandatoryProperty
    private final Boolean advancedRequestLoggingEnabled;

    public static LoggingConfigs random() {
        return randomBoolean() ? enabled() : disabled();
    }

    public static LoggingConfigs testsHardcoded() {
        return LoggingConfigs.enabled();
    }

    public static LoggingConfigs enabled() {
        return new LoggingConfigs(true);
    }

    public static LoggingConfigs disabled() {
        return new LoggingConfigs(false);
    }

    public boolean isAdvancedRequestLoggingEnabled() {
        return this.advancedRequestLoggingEnabled;
    }
}
