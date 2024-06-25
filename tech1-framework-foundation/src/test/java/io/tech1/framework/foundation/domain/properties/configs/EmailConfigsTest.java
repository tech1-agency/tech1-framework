package io.tech1.framework.foundation.domain.properties.configs;

import io.tech1.framework.foundation.domain.base.Email;
import io.tech1.framework.foundation.domain.base.Password;
import io.tech1.framework.foundation.domain.base.Username;
import org.junit.jupiter.api.Test;

import static io.tech1.framework.foundation.utilities.random.RandomUtility.randomIntegerGreaterThanZero;
import static io.tech1.framework.foundation.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;

class EmailConfigsTest {

    @Test
    void constructorTest() {
        // Act
        var emailConfigs = new EmailConfigs(
                true,
                randomString(),
                randomIntegerGreaterThanZero(),
                Username.random().value(),
                Username.random(),
                Password.random()
        );

        // Assert
        assertThat(emailConfigs.isEnabled()).isTrue();
        assertThat(emailConfigs.getHost()).isNotNull();
        assertThat(emailConfigs.getPort()).isNotZero();
        assertThat(emailConfigs.getFrom()).isNotNull();
        assertThat(emailConfigs.getUsername()).isNotNull();
        assertThat(emailConfigs.getPassword()).isNotNull();
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
    }

    @Test
    void enabledTest() {
        // Arrange
        var from = Email.random().value();

        // Act
        var emailConfigs = EmailConfigs.enabled(from);

        // Assert
        assertThat(emailConfigs.isEnabled()).isTrue();
        assertThat(emailConfigs.getHost()).isNotNull();
        assertThat(emailConfigs.getPort()).isEqualTo(587);
        assertThat(emailConfigs.getFrom()).isEqualTo(from);
        assertThat(emailConfigs.getUsername()).isEqualTo(Username.testsHardcoded());
        assertThat(emailConfigs.getPassword()).isEqualTo(Password.testsHardcoded());
    }
}
