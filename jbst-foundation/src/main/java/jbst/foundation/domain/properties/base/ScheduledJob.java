package jbst.foundation.domain.properties.base;

import jbst.foundation.domain.properties.annotations.MandatoryProperty;
import jbst.foundation.domain.properties.annotations.MandatoryToggleProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import static jbst.foundation.utilities.random.RandomUtility.randomBoolean;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class ScheduledJob extends AbstractTogglePropertyConfigs {
    @MandatoryProperty
    private final boolean enabled;
    @MandatoryToggleProperty
    private SchedulerConfiguration configuration;

    public static ScheduledJob testsHardcoded() {
        return new ScheduledJob(true, SchedulerConfiguration.testsHardcoded());
    }

    public static ScheduledJob random() {
        return randomBoolean() ? enabled() : disabled();
    }

    public static ScheduledJob enabled() {
        return testsHardcoded();
    }


    public static ScheduledJob disabled() {
        return new ScheduledJob(false, null);
    }
}
