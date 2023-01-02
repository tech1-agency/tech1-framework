package io.tech1.framework.domain.properties.configs;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IncidentConfigsTest {

    @Test
    public void constructorTest() {
        // Act
        var incidentConfigs = new IncidentConfigs();

        // Assert
        assertThat(incidentConfigs.isEnabled()).isFalse();
        assertThat(incidentConfigs.getRemoteServer()).isNull();
    }

    @Test
    public void disabledTest() {
        // Act
        var incidentConfigs = IncidentConfigs.disabled();

        // Assert
        assertThat(incidentConfigs.isEnabled()).isFalse();
        assertThat(incidentConfigs.getRemoteServer()).isNull();
    }
}
