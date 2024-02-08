package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static org.assertj.core.api.Assertions.assertThat;

class EmailConfigsTest {

    @Test
    void constructorTest() {
        // Act
        var emailConfigs = new EmailConfigs(
                true,
                randomString(),
                randomIntegerGreaterThanZero(),
                Username.random().identifier(),
                Username.random(),
                Password.random(),
                randomStringsAsArray(3)
        );

        // Assert
        assertThat(emailConfigs.isEnabled()).isTrue();
        assertThat(emailConfigs.getHost()).isNotNull();
        assertThat(emailConfigs.getPort()).isNotZero();
        assertThat(emailConfigs.getFrom()).isNotNull();
        assertThat(emailConfigs.getUsername()).isNotNull();
        assertThat(emailConfigs.getPassword()).isNotNull();
        assertThat(emailConfigs.getTo()).isNotNull();
        assertThat(emailConfigs.getTo()).hasSize(3);
    }

    @Test
    void disabledTest() {
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
    void enabledTest() {
        // Arrange
        var from = Email.random().value();

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
