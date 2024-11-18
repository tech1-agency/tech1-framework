package jbst.iam.domain.mongodb;

import tech1.framework.foundation.domain.base.Password;
import tech1.framework.foundation.domain.base.Username;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

import static tech1.framework.foundation.utilities.random.RandomUtility.randomBoolean;
import static tech1.framework.foundation.utilities.random.RandomUtility.randomZoneId;
import static tech1.framework.foundation.utilities.reflections.ReflectionUtility.setPrivateField;
import static org.assertj.core.api.Assertions.assertThat;

class MongoDbUserTest {

    @Test
    void getNotNullAttributesTest() {
        // Arrange
        var user = new MongoDbUser(
                Username.random(),
                Password.random(),
                randomZoneId(),
                Set.of(
                        new SimpleGrantedAuthority("admin123")
                ),
                randomBoolean()
        );

        // Act
        var actual = user.getNotNullAttributes();

        // Assert
        assertThat(actual).isEmpty();
    }

    @Test
    void getNotNullAttributesLegacyMigrationNullPointerExceptionTest() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        var user = new MongoDbUser(
                Username.random(),
                Password.random(),
                randomZoneId(),
                Set.of(
                        new SimpleGrantedAuthority("admin123")
                ),
                randomBoolean()
        );
        setPrivateField(user, "attributes", null);

        // Act
        var actual = user.getNotNullAttributes();

        // Assert
        assertThat(actual).isEmpty();
    }
}
