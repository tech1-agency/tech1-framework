package jbst.iam.domain.mongodb;

import jbst.iam.domain.db.UserEmailDetails;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import jbst.foundation.domain.base.Password;
import jbst.foundation.domain.base.Username;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static jbst.foundation.utilities.random.RandomUtility.randomBoolean;
import static jbst.foundation.utilities.random.RandomUtility.randomZoneId;
import static jbst.foundation.utilities.reflections.ReflectionUtility.setPrivateField;

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
                randomBoolean(),
                UserEmailDetails.random()
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
                randomBoolean(),
                UserEmailDetails.random()
        );
        setPrivateField(user, "attributes", null);

        // Act
        var actual = user.getNotNullAttributes();

        // Assert
        assertThat(actual).isEmpty();
    }
}
