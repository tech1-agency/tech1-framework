package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import lombok.Data;

// Lombok (property-based)
@Data
public class ScheduledJob implements AbstractToggleProperty {
    @MandatoryProperty
    private boolean enabled;
    @MandatoryProperty
    private SchedulerConfiguration configuration;

    // NOTE: test-purposes
    public static ScheduledJob of(
            boolean enabled,
            SchedulerConfiguration configuration
    ) {
        var instance = new ScheduledJob();
        instance.enabled = enabled;
        instance.configuration = configuration;
        return instance;
    }

    // NOTE: test-purposes
    public static ScheduledJob enabled() {
        return ScheduledJob.of(
                true,
                null
        );
    }


    // NOTE: test-purposes
    public static ScheduledJob disabled() {
        return ScheduledJob.of(
                false,
                null
        );
    }
}
