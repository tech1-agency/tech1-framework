package io.tech1.framework.b2b.base.security.jwt.domain.security;

import io.tech1.framework.b2b.base.security.jwt.tests.domain.enums.TestAuthority;
import io.tech1.framework.domain.base.AbstractAuthority;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.constants.DomainConstants;
import io.tech1.framework.domain.constants.ZoneIdsConstants;
import io.tech1.framework.domain.tests.runners.AbstractFolderSerializationRunner;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static io.tech1.framework.domain.tests.constants.TestsJunitConstants.FIVE_TIMES;
import static io.tech1.framework.domain.tests.io.TestsIOUtils.readFile;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomZoneId;
import static io.tech1.framework.domain.utilities.reflections.ReflectionUtility.setPrivateField;
import static org.assertj.core.api.Assertions.assertThat;

class CurrentClientUserTest extends AbstractFolderSerializationRunner {

    private static Stream<Arguments> getAttributeByKeyTest() {
        return Stream.of(
                Arguments.of("key2", false, "value2"),
                Arguments.of("key2", true, null),
                Arguments.of("key3", false, 1L),
                Arguments.of("key3", true, null),
                Arguments.of("key4", false, null),
                Arguments.of(randomString(), false, null)
        );
    }

    private static Stream<Arguments> hasAbstractAuthorityTest() {
        return Stream.of(
                Arguments.of(TestAuthority.INVITATION_CODE_READ, false),
                Arguments.of(TestAuthority.INVITATION_CODE_WRITE, false),
                Arguments.of(TestAuthority.ADMIN, true)
        );
    }

    private static Stream<Arguments> hasAuthorityTest() {
        return Stream.of(
                Arguments.of("user2", true),
                Arguments.of(AbstractAuthority.SUPER_ADMIN, true),
                Arguments.of(AbstractAuthority.INVITATION_CODE_READ, false),
                Arguments.of(AbstractAuthority.INVITATION_CODE_WRITE, false)
        );
    }

    @Override
    protected String getFolder() {
        return "security";
    }

    @RepeatedTest(FIVE_TIMES)
    void serializeTest() {
        // Arrange
        var currentClientUser = new CurrentClientUser(
                Username.of("tech1"),
                Email.of("tech1@" + DomainConstants.TECH1),
                "Tech1",
                ZoneIdsConstants.UKRAINE,
                Set.of(
                        new SimpleGrantedAuthority("user"),
                        new SimpleGrantedAuthority("admin")
                ),
                Map.of(
                        "key1", "value1",
                        "key2", 2L
                )
        );

        // Act
        var json = this.writeValueAsString(currentClientUser);

        // Assert
        assertThat(json).isEqualTo(readFile(this.getFolder(), "current-client-user.json"));
    }

    @ParameterizedTest
    @MethodSource("getAttributeByKeyTest")
    void getAttributeByKeyTest(String attributeKey, boolean reflectionHack, Object expected) throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        var currentClientUser = new CurrentClientUser(
                Username.random(),
                Email.random(),
                randomString(),
                randomZoneId(),
                Set.of(),
                Map.of(
                        "key1", new Object(),
                        "key2", "value2",
                        "key3", 1L
                )
        );
        if (reflectionHack) {
            setPrivateField(currentClientUser, "attributes", null);
        }

        // Act
        var actual = currentClientUser.getAttributeByKey(attributeKey);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("hasAbstractAuthorityTest")
    void hasAbstractAuthorityTest(AbstractAuthority abstractAuthority, boolean expected) {
        // Arrange
        var currentClientUser = new CurrentClientUser(
                Username.random(),
                Email.random(),
                randomString(),
                randomZoneId(),
                Set.of(
                        new SimpleGrantedAuthority("user1"),
                        new SimpleGrantedAuthority("user2"),
                        new SimpleGrantedAuthority("admin")
                ),
                Map.of()
        );

        // Act
        var actual = currentClientUser.hasAuthority(abstractAuthority);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("hasAuthorityTest")
    void hasAuthorityTest(String authority, boolean expected) {
        // Arrange
        var currentClientUser = new CurrentClientUser(
                Username.random(),
                Email.random(),
                randomString(),
                randomZoneId(),
                Set.of(
                        new SimpleGrantedAuthority("user1"),
                        new SimpleGrantedAuthority("user2"),
                        new SimpleGrantedAuthority("superadmin")
                ),
                Map.of()
        );

        // Act
        var actual = currentClientUser.hasAuthority(authority);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }
}
