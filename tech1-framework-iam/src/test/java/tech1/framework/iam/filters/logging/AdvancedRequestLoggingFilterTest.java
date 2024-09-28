package tech1.framework.iam.filters.logging;

import tech1.framework.iam.utils.HttpRequestUtils;
import tech1.framework.iam.utils.SecurityPrincipalUtils;
import tech1.framework.foundation.domain.http.cache.CachedBodyHttpServletRequest;
import tech1.framework.foundation.domain.http.cache.CachedBodyServletInputStream;
import tech1.framework.foundation.domain.properties.configs.SecurityJwtConfigs;
import tech1.framework.foundation.domain.properties.configs.security.jwt.LoggingConfigs;
import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AdvancedRequestLoggingFilterTest {

    @Configuration
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        @Bean
        ApplicationFrameworkProperties applicationFrameworkProperties() {
            return mock(ApplicationFrameworkProperties.class);
        }

        @Bean
        HttpRequestUtils httpRequestUtility() {
            return mock(HttpRequestUtils.class);
        }

        @Bean
        SecurityPrincipalUtils securityPrincipalUtility() {
            return mock(SecurityPrincipalUtils.class);
        }

        @Bean
        AdvancedRequestLoggingFilter advancedRequestLoggingFilter() {
            return new AdvancedRequestLoggingFilter(
                    this.httpRequestUtility(),
                    this.securityPrincipalUtility(),
                    this.applicationFrameworkProperties()
            );
        }
    }

    private final HttpRequestUtils httpRequestUtils;
    private final SecurityPrincipalUtils securityPrincipalUtils;
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final AdvancedRequestLoggingFilter componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.httpRequestUtils,
                this.securityPrincipalUtils
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.httpRequestUtils,
                this.securityPrincipalUtils
        );
    }

    @Test
    void multipartRequestEndpointTest() throws ServletException, IOException {
        // Arrange
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(SecurityJwtConfigs.of(LoggingConfigs.random()));
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var filterChain = mock(FilterChain.class);
        when(request.getMethod()).thenReturn("POST");
        when(request.getContentType()).thenReturn("multipart/form-data");

        // Act
        this.componentUnderTest.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(any(HttpServletRequest.class), eq(response));
        verify(request).getContentType();
        verify(request).getMethod();
        verifyNoMoreInteractions(
                request,
                response,
                filterChain
        );
    }

    @Test
    void disabledLoggingCachedEndpointTest() throws ServletException, IOException {
        // Arrange
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(SecurityJwtConfigs.of(LoggingConfigs.disabled()));
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var filterChain = mock(FilterChain.class);

        // Act
        this.componentUnderTest.doFilterInternal(request, response, filterChain);

        // Assert
        verify(this.httpRequestUtils).cachePayload(any());
        verify(filterChain).doFilter(any(CachedBodyHttpServletRequest.class), eq(response));
        verify(request).getContentType();
        verify(request).getInputStream();
        verifyNoMoreInteractions(
                request,
                response,
                filterChain
        );
    }

    @Test
    void enabledLoggingEmptyPayloadTest() throws ServletException, IOException {
        // Arrange
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(SecurityJwtConfigs.of(LoggingConfigs.enabled()));
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var filterChain = mock(FilterChain.class);
        when(request.getInputStream()).thenReturn(new CachedBodyServletInputStream("".getBytes()));

        // Act
        this.componentUnderTest.doFilterInternal(request, response, filterChain);

        // Assert
        verify(this.httpRequestUtils).cachePayload(any());
        verify(this.securityPrincipalUtils).getAuthenticatedUsernameOrUnexpected();
        verify(request).getContentType();
        verify(request).getInputStream();
        verify(request).getMethod();
        verify(request).getServletPath();
        verify(filterChain).doFilter(any(CachedBodyHttpServletRequest.class), eq(response));
        verifyNoMoreInteractions(
                request,
                response,
                filterChain
        );
    }

    @Test
    void enabledLoggingTest() throws ServletException, IOException {
        // Arrange
        when(this.applicationFrameworkProperties.getSecurityJwtConfigs()).thenReturn(SecurityJwtConfigs.of(LoggingConfigs.enabled()));
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var filterChain = mock(FilterChain.class);
        when(request.getInputStream()).thenReturn(new CachedBodyServletInputStream("{}".getBytes()));

        // Act
        this.componentUnderTest.doFilterInternal(request, response, filterChain);

        // Assert
        verify(this.httpRequestUtils).cachePayload(any());
        verify(this.securityPrincipalUtils).getAuthenticatedUsernameOrUnexpected();
        verify(request).getContentType();
        verify(request).getInputStream();
        verify(request).getMethod();
        verify(request).getServletPath();
        verify(filterChain).doFilter(any(CachedBodyHttpServletRequest.class), eq(response));
        verifyNoMoreInteractions(
                request,
                response,
                filterChain
        );
    }
}
