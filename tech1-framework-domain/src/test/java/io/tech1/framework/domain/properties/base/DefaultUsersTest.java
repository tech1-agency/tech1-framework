package io.tech1.framework.domain.properties.base;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.time.ZoneId.systemDefault;
import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;

public class DefaultUsersTest {

    private static Stream<Arguments> getDefaultUsersAuthoritiesTest() {
        return Stream.of(
                Arguments.of(null, emptySet()),
                Arguments.of(
                        List.of(
                                DefaultUser.of("user1", "pass1", systemDefault(), null)
                        ),
                        emptySet()
                ),
                Arguments.of(
                        List.of(
                                DefaultUser.of("user1", "pass1", systemDefault(), List.of("user")),
                                DefaultUser.of("user2", "pass2", systemDefault(), List.of("admin", "user"))
                        ),
                        Set.of("user", "admin")
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getDefaultUsersAuthoritiesTest")
    public void getDefaultUsersAuthoritiesTest(List<DefaultUser> users, Set<String> expected) {
        // Arrange
        var defaultUsers = DefaultUsers.of(true, users);

        // Act
       var actual = defaultUsers.getDefaultUsersAuthorities();

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
    }
}
