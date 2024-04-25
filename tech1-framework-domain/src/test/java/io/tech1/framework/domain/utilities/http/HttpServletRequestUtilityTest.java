package io.tech1.framework.domain.utilities.http;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.http.HttpServletRequestUtility.getBaseURL;
import static io.tech1.framework.domain.utilities.http.HttpServletRequestUtility.getFullURL;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static java.util.Objects.isNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HttpServletRequestUtilityTest {

    private static Stream<Arguments> getBaseURLTest() {
        return Stream.of(
                Arguments.of("http://localhost", "http://localhost"),
                Arguments.of("https://localhost", "https://localhost"),
                Arguments.of("http://localhost:8080", "http://localhost:8080"),
                Arguments.of("https://localhost:8080", "https://localhost:8080"),
                Arguments.of("https://www.tech1.io", "https://www.tech1.io"),
                Arguments.of("www.tech1.io", "www.tech1.io"),
                Arguments.of("http://localhost:8080?attr1=value", "http://localhost:8080"),
                Arguments.of("http://localhost:8080?attr1=value&attr2=value2", "http://localhost:8080")
        );
    }

    private static Stream<Arguments> getFullURLTest() {
        return Stream.of(
                Arguments.of(randomString(), null),
                Arguments.of(randomString(), randomString())
        );
    }

    @ParameterizedTest
    @MethodSource("getBaseURLTest")
    void getBaseURLTest(String url, String expected) {
        // Act
        var actual = getBaseURL(url);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getFullURLExceptionTest() {
        // Act
        var throwable1 = catchThrowable(() -> getFullURL(null));
        var throwable2 = catchThrowable(() -> getFullURL(mock(HttpServletRequest.class)));

        // Assert
        assertThat(throwable1).isNotNull();
        assertThat(throwable1.getClass()).isEqualTo(IllegalArgumentException.class);
        assertThat(throwable1.getMessage()).isEqualTo("Attribute `request` is invalid");
        assertThat(throwable2).isNotNull();
        assertThat(throwable2.getClass()).isEqualTo(IllegalArgumentException.class);
        assertThat(throwable2.getMessage()).isEqualTo("Attribute `request.requestURL` is invalid");
    }

    @ParameterizedTest
    @MethodSource("getFullURLTest")
    void getFullURLTest(String requestURL, String queryString) {
        // Arrange
        var request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer(requestURL));
        when(request.getQueryString()).thenReturn(queryString);

        // Act
        var actual = getFullURL(request);

        // Assert
        assertThat(actual).isNotNull();
        if (isNull(queryString)) {
            assertThat(actual).isEqualTo(requestURL);
        } else {
            assertThat(actual).isEqualTo(requestURL + "?" + queryString);
        }
    }
}
