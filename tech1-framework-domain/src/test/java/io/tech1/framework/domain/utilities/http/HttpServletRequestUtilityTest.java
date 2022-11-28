package io.tech1.framework.domain.utilities.http;

import io.tech1.framework.domain.http.requests.UserAgentHeader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.http.HttpServletRequestUtility.*;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static java.util.Objects.isNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.*;

public class HttpServletRequestUtilityTest {

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

    private static Stream<Arguments> getUserAgentDetailsTest() {
        return Stream.of(
                Arguments.of("", "Unknown", "Unknown", "Unknown"),
                Arguments.of(randomString(), "Default Browser", "Unknown", "Unknown"),
                Arguments.of("Mozilla/5.0 (Macintosh; Intel Mac OS X x.y; rv:42.0) Gecko/20100101 Firefox/42.0", "Firefox", "MacOSX", "Desktop")
        );
    }

    @ParameterizedTest
    @MethodSource("getBaseURLTest")
    public void getBaseURLTest(String url, String expected) {
        // Act
        var actual = getBaseURL(url);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
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

    @Test
    public void getUserAgentDetailsExceptionTest() {
        // Arrange
        var message = randomString();
        var userAgentHeader = mock(UserAgentHeader.class);
        doAnswer(invocation -> { throw new IOException(message); } ).when(userAgentHeader).getValue();

        // Act
        var userAgentDetails = getUserAgentDetails(userAgentHeader);

        // Assert
        assertThat(userAgentDetails).isNotNull();
        assertThat(userAgentDetails.getBrowser()).isEqualTo("Unknown");
        assertThat(userAgentDetails.getPlatform()).isEqualTo("Unknown");
        assertThat(userAgentDetails.getDeviceType()).isEqualTo("Unknown");
        assertThat(userAgentDetails.getExceptionDetails()).isEqualTo(message);
        assertThat(userAgentDetails.getWhat()).isEqualTo("Unknown, Unknown on Unknown");
    }

    @ParameterizedTest
    @MethodSource("getUserAgentDetailsTest")
    public void getUserAgentDetailsTest(String header, String browser, String platform, String deviceType) {
        // Arrange
        var request = mock(HttpServletRequest.class);
        when(request.getHeader("User-Agent")).thenReturn(header);
        var userAgentHeader = new UserAgentHeader(request);

        // Act
        var userAgentDetails = getUserAgentDetails(userAgentHeader);

        // Assert
        assertThat(userAgentDetails).isNotNull();
        assertThat(userAgentDetails.getBrowser()).isEqualTo(browser);
        assertThat(userAgentDetails.getPlatform()).isEqualTo(platform);
        assertThat(userAgentDetails.getDeviceType()).isEqualTo(deviceType);
        assertThat(userAgentDetails.getExceptionDetails()).isEqualTo("");
    }
}
