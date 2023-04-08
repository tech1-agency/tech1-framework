package io.tech1.framework.domain.properties.configs;

import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static org.assertj.core.api.Assertions.assertThat;

public class EmailConfigsTest {

    @Test
    public void constructorTest() {
        // Act
        var emailConfigs = EmailConfigs.of(
                true,
                randomString(),
                randomInteger(),
                randomUsername().getIdentifier(),
                randomString(),
                randomString(),
                randomStringsAsArray(3)
        );

        // Assert
        assertThat(emailConfigs.isEnabled()).isTrue();
        assertThat(emailConfigs.getHost()).isNotNull();
        assertThat(emailConfigs.getPort()).isNotNull();
        assertThat(emailConfigs.getFrom()).isNotNull();
        assertThat(emailConfigs.getUsername()).isNotNull();
        assertThat(emailConfigs.getPassword()).isNotNull();
        assertThat(emailConfigs.getTo()).isNotNull();
        assertThat(emailConfigs.getTo()).hasSize(3);
    }

    @Test
    public void disabledTest() {
        // Act
        var emailConfigs = EmailConfigs.disabled();

        // Assert
        assertThat(emailConfigs.isEnabled()).isFalse();
        assertThat(emailConfigs.getHost()).isNull();
        assertThat(emailConfigs.getPort()).isZero();
        assertThat(emailConfigs.getFrom()).isNull();
        assertThat(emailConfigs.getUsername()).isNull();
        assertThat(emailConfigs.getPassword()).isNull();
        assertThat(emailConfigs.getTo()).isNull();
    }

    @Test
    public void enabledTest() {
        // Arrange
        var from = randomEmailAsValue();

        // Act
        var emailConfigs = EmailConfigs.enabled(from);

        // Assert
        assertThat(emailConfigs.isEnabled()).isTrue();
        assertThat(emailConfigs.getHost()).isNull();
        assertThat(emailConfigs.getPort()).isZero();
        assertThat(emailConfigs.getFrom()).isEqualTo(from);
        assertThat(emailConfigs.getUsername()).isNull();
        assertThat(emailConfigs.getPassword()).isNull();
        assertThat(emailConfigs.getTo()).isNull();
    }
}
