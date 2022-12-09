package io.tech1.framework.domain.utilities.http;

import io.tech1.framework.domain.base.Email;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.http.HttpRequestFieldsUtility.containsCamelCaseLettersAndNumbersWithLength;
import static io.tech1.framework.domain.utilities.http.HttpRequestFieldsUtility.isEmail;
import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestFieldsUtilityTest {

    private static Stream<Arguments> containsCamelCaseLettersAndNumbersWithLengthTest() {
        return Stream.of(
                Arguments.of("only_lowercase", 5, false),
                Arguments.of("only_lowercase", 100, false),
                Arguments.of("ONLY_UPPERCASE", 5, false),
                Arguments.of("ONLY_UPPERCASE", 100, false),
                Arguments.of("BoTh_CaSeS", 5, false),
                Arguments.of("BoTh_CaSeS", 100, false),
                Arguments.of("BoTh_CaSeS123", 100, false),
                Arguments.of("BoTh_CaSeS123", 5, true)
        );
    }

    private static Stream<Arguments> isEmailTest() {
        return Stream.of(
                Arguments.of("info", false),
                Arguments.of("info@", false),
                Arguments.of("info@tech1", false),
                Arguments.of("info@tech1.io", true),
                Arguments.of("petro.petrenko@gmail.com", true),
                Arguments.of("john78@proton.com", true)
        );
    }

    @ParameterizedTest
    @MethodSource("containsCamelCaseLettersAndNumbersWithLengthTest")
    public void containsCamelCaseLettersAndNumbersWithLengthTest(String field, int length, boolean expected) {
        // Act
        var actual = containsCamelCaseLettersAndNumbersWithLength(field, length);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("isEmailTest")
    public void isEmailTest(String email, boolean expected) {
        // Act
        var actual1 = isEmail(Email.of(email));
        var actual2 = isEmail(email);

        // Assert
        assertThat(actual1).isEqualTo(expected);
        assertThat(actual2).isEqualTo(expected);
    }
}
