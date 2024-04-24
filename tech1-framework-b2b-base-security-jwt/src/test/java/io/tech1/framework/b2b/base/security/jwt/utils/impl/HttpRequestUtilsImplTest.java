package io.tech1.framework.b2b.base.security.jwt.utils.impl;

import io.tech1.framework.b2b.base.security.jwt.utils.HttpRequestUtils;
import io.tech1.framework.domain.http.cache.CachedBodyHttpServletRequest;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import io.tech1.framework.properties.tests.contexts.ApplicationFrameworkPropertiesContext;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

import static io.tech1.framework.b2b.base.security.jwt.utils.impl.HttpRequestUtilsImpl.CACHED_PAYLOAD_ATTRIBUTE;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@TestPropertySource(properties = {
        "server.servlet.contextPath=/api"
})
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class HttpRequestUtilsImplTest {

    private static Stream<Arguments> authenticationLoginEndpointCases() {
        return Stream.of(
                Arguments.of("GET", "/api/framework/security/authentication/login", false),
                Arguments.of("PUT", "/api/framework/security/authentication/login", false),
                Arguments.of("POST", "/api/framework/security/authentication/login", true),
                Arguments.of("GET", "/api/framework/security/authentication/login1", false),
                Arguments.of("PUT", "/api/framework/security/authentication/login1", false),
                Arguments.of("POST", "/api/framework/security/authentication/login1", false)
        );
    }

    private static Stream<Arguments> authenticationRefreshTokenEndpointCases() {
        return Stream.of(
                Arguments.of("GET", "/api/framework/security/authentication/refreshToken", false),
                Arguments.of("PUT", "/api/framework/security/authentication/refreshToken", false),
                Arguments.of("POST", "/api/framework/security/authentication/refreshToken", true),
                Arguments.of("GET", "/api/framework/security/authentication/refreshToken1", false),
                Arguments.of("PUT", "/api/framework/security/authentication/refreshToken1", false),
                Arguments.of("POST", "/api/framework/security/authentication/refreshToken1", false)
        );
    }

    @Configuration
    @Import({
            ApplicationFrameworkPropertiesContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        // Properties
        private final ApplicationFrameworkProperties applicationFrameworkProperties;

        @Bean
        HttpRequestUtils httpRequestUtility() {
            return new HttpRequestUtilsImpl(
                    this.applicationFrameworkProperties
            );
        }
    }

    private final HttpRequestUtils componentUnderTest;

    @ParameterizedTest
    @MethodSource("authenticationLoginEndpointCases")
    void isCachedEndpointTest(String method, String requestURI, boolean expected) {
        // Arrange
        var cachedRequest = mock(CachedBodyHttpServletRequest.class);
        when(cachedRequest.getMethod()).thenReturn(method);
        when(cachedRequest.getRequestURI()).thenReturn(requestURI);

        // Act
        var actual = this.componentUnderTest.isCachedEndpoint(cachedRequest);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void cachePayloadTest() {
        // Arrange
        var cachedRequest = mock(CachedBodyHttpServletRequest.class);
        var payload = randomString();

        // Act
        this.componentUnderTest.cachePayload(cachedRequest, payload);

        // Assert
        verify(cachedRequest).setAttribute(CACHED_PAYLOAD_ATTRIBUTE, payload);
        verifyNoMoreInteractions(cachedRequest);
    }

    @Test
    void getCachedPayloadMissingTest() {
        // Arrange
        var request = mock(HttpServletRequest.class);
        var payload = randomString();
        when(request.getAttribute(CACHED_PAYLOAD_ATTRIBUTE)).thenReturn(payload);

        // Act
        var cachedPayload = this.componentUnderTest.getCachedPayload(request);

        // Assert
        assertThat(cachedPayload).isEqualTo(payload);
        verify(request).getAttribute(CACHED_PAYLOAD_ATTRIBUTE);
        verifyNoMoreInteractions(request);
    }

    @Test
    void getCachedPayloadPresentTest() {
        // Arrange
        var request = mock(HttpServletRequest.class);

        // Act
        var cachedPayload = this.componentUnderTest.getCachedPayload(request);

        // Assert
        assertThat(cachedPayload).isNull();
        verify(request).getAttribute(CACHED_PAYLOAD_ATTRIBUTE);
        verifyNoMoreInteractions(request);
    }

    @ParameterizedTest
    @MethodSource("authenticationLoginEndpointCases")
    void isAuthenticationLoginEndpointTest(String method, String requestURI, boolean expected) {
        // Arrange
        var cachedRequest = mock(CachedBodyHttpServletRequest.class);
        when(cachedRequest.getMethod()).thenReturn(method);
        when(cachedRequest.getRequestURI()).thenReturn(requestURI);

        // Act
        var actual = this.componentUnderTest.isAuthenticationLoginEndpoint(cachedRequest);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("authenticationRefreshTokenEndpointCases")
    void isAuthenticationRefreshTokenEndpointTest(String method, String requestURI, boolean expected) {
        // Arrange
        var cachedRequest = mock(CachedBodyHttpServletRequest.class);
        when(cachedRequest.getMethod()).thenReturn(method);
        when(cachedRequest.getRequestURI()).thenReturn(requestURI);

        // Act
        var actual = this.componentUnderTest.isAuthenticationRefreshTokenEndpoint(cachedRequest);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }
}
