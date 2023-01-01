package io.tech1.framework.domain.time;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomEnum;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomLongGreaterThanZero;
import static org.assertj.core.api.Assertions.assertThat;

public class SchedulerConfigurationTest {

    @Test
    public void neverTest() {
        // Act
        var sc = SchedulerConfiguration.never();

        // Assert
        assertThat(sc).isNotNull();
        assertThat(sc.getInitialDelay()).isEqualTo(9223372036854775807L);
        assertThat(sc.getDelay()).isEqualTo(9223372036854775807L);
        assertThat(sc.getUnit()).isEqualTo(TimeUnit.DAYS);
        assertThat(sc.getUnit().toSeconds(sc.getDelay())).isEqualTo(9223372036854775807L);
    }

    @Test
    public void constructorTest() {
        // Arrange
        var initialDelay = randomLongGreaterThanZero();
        var delay = randomLongGreaterThanZero();
        var unit = randomEnum(TimeUnit.class);

        // Act
        var schedulerConfiguration = SchedulerConfiguration.of(initialDelay, delay, unit);

        // Assert
        assertThat(schedulerConfiguration).isNotNull();
        assertThat(schedulerConfiguration.getInitialDelay()).isEqualTo(initialDelay);
        assertThat(schedulerConfiguration.getDelay()).isEqualTo(delay);
        assertThat(schedulerConfiguration.getUnit()).isEqualTo(unit);
    }
}
