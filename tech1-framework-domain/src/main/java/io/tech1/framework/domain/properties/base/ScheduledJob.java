package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConstructorBinding;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
public class ScheduledJob implements AbstractToggleProperty {
    @MandatoryProperty
    private final boolean enabled;
    @MandatoryProperty
    private SchedulerConfiguration configuration;

    public static ScheduledJob enabled() {
        return new ScheduledJob(
                true,
                null
        );
    }


    public static ScheduledJob disabled() {
        return new ScheduledJob(
                false,
                null
        );
    }
}
