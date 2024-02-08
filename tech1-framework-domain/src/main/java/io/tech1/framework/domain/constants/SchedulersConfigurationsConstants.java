package io.tech1.framework.domain.constants;

import io.tech1.framework.domain.time.SchedulerConfiguration;
import lombok.experimental.UtilityClass;

import static java.util.concurrent.TimeUnit.*;

@UtilityClass
public class SchedulersConfigurationsConstants {
    public static final SchedulerConfiguration EVERY_5_SECONDS = new SchedulerConfiguration(5L, 5L, SECONDS);
    public static final SchedulerConfiguration EVERY_15_SECONDS = new SchedulerConfiguration(15L, 15L, SECONDS);
    public static final SchedulerConfiguration EVERY_30_SECONDS = new SchedulerConfiguration(30L, 30L, SECONDS);
    public static final SchedulerConfiguration EVERY_45_SECONDS = new SchedulerConfiguration(45L, 45L, SECONDS);
    public static final SchedulerConfiguration EVERY_1_MINUTE = new SchedulerConfiguration(1L, 1L, MINUTES);
    public static final SchedulerConfiguration EVERY_5_MINUTES = new SchedulerConfiguration(5L, 5L, MINUTES);
    public static final SchedulerConfiguration EVERY_9_MINUTES = new SchedulerConfiguration(9L, 9L, MINUTES);
    public static final SchedulerConfiguration EVERY_15_MINUTES = new SchedulerConfiguration(15L, 15L, MINUTES);
    public static final SchedulerConfiguration EVERY_30_MINUTES = new SchedulerConfiguration(30L, 30L, MINUTES);
    public static final SchedulerConfiguration EVERY_1_HOUR = new SchedulerConfiguration(1L, 1L, HOURS);
    public static final SchedulerConfiguration EVERY_12_HOURS = new SchedulerConfiguration(12L, 12L, HOURS);
}
