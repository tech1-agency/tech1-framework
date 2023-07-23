package io.tech1.framework.b2b.mongodb.security.jwt.domain.db;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static io.tech1.framework.domain.utilities.reflections.ReflectionUtility.setPrivateField;
import static org.assertj.core.api.Assertions.assertThat;

class DbUserTest {

    @Test
    void getNotNullAttributesTest() {
        // Arrange
        var user = new DbUser(
                randomUsername(),
                randomPassword(),
                randomZoneId().getId(),
                List.of(
                        new SimpleGrantedAuthority("admin123")
                )
        );

        // Act
        var actual = user.getNotNullAttributes();

        // Assert
        assertThat(actual).isEmpty();
    }

    @Test
    void getNotNullAttributesLegacyMigrationNullPointerExceptionTest() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        var user = new DbUser(
                randomUsername(),
                randomPassword(),
                randomZoneId().getId(),
                List.of(
                        new SimpleGrantedAuthority("admin123")
                )
        );
        setPrivateField(user, "attributes", null);

        // Act
        var actual = user.getNotNullAttributes();

        // Assert
        assertThat(actual).isEmpty();
    }
}
