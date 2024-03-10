package io.tech1.framework.incidents.domain.authetication;

import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.base.UsernamePasswordCredentials;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class IncidentAuthenticationLoginFailureUsernameMaskedPasswordTest {

    private static Stream<Arguments> ofTest() {
        return Stream.of(
                Arguments.of("123456789", "12345****"),
                Arguments.of("12345", "12345"),
                Arguments.of("abcdefghij", "abcde*****"),
                Arguments.of("abc", "abc")
        );
    }

    @ParameterizedTest
    @MethodSource("ofTest")
    void ofTest(String password, String expected) {
        // Arrange
        var username = Username.random();


        // Act
        var actual = new IncidentAuthenticationLoginFailureUsernameMaskedPassword(
                UsernamePasswordCredentials.mask5(
                        username,
                        new Password(password)
                )
        );

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.credentials().username()).isEqualTo(username);
        assertThat(actual.credentials().password().value()).isEqualTo(expected);
    }
}
