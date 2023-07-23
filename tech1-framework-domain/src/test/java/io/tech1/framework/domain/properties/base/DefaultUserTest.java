package io.tech1.framework.domain.properties.base;

import io.tech1.framework.domain.tests.constants.TestsPropertiesConstants;
import org.junit.jupiter.api.Test;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static org.assertj.core.api.Assertions.assertThat;

class DefaultUserTest {

    @Test
    void getEmailNullTest() {
        // Arrange
        var defaultUsers = TestsPropertiesConstants.SECURITY_JWT_CONFIGS.getEssenceConfigs().getDefaultUsers();

        // Act
        var email = defaultUsers.getUsers().get(0).getEmail();

        // Assert
        assertThat(email).isNull();
    }

    @Test
    void getEmailTest() {
        // Arrange
        var defaultUsers = entity(DefaultUser.class);

        // Act
        var email = defaultUsers.getEmail();

        // Assert
        assertThat(email).isNotNull();
    }
}
