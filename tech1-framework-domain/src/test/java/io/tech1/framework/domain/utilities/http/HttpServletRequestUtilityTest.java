package io.tech1.framework.domain.utilities.http;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.http.HttpServletRequestUtility.getFullURL;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static java.util.Objects.isNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpServletRequestUtilityTest {

    private static Stream<Arguments> getFullURLTest() {
        return Stream.of(
                Arguments.of(randomString(), null),
                Arguments.of(randomString(), randomString())
        );
    }

    @Test
    public void getFullURLExceptionTest() {
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
    public void getFullURLTest(String requestURL, String queryString) {
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
