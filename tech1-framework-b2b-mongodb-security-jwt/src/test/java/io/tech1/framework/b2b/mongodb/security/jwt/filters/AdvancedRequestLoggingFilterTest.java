package io.tech1.framework.b2b.mongodb.security.jwt.filters;

import io.tech1.framework.b2b.mongodb.security.jwt.utilities.HttpRequestUtility;
import io.tech1.framework.b2b.mongodb.security.jwt.utilities.SecurityPrincipalUtility;
import io.tech1.framework.domain.http.cache.CachedBodyHttpServletRequest;
import io.tech1.framework.domain.http.cache.CachedBodyServletInputStream;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import io.tech1.framework.properties.tests.contexts.ApplicationFrameworkPropertiesContext;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdvancedRequestLoggingFilterTest {

    @Configuration
    @Import({
            ApplicationFrameworkPropertiesContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        // Properties
        private final ApplicationFrameworkProperties applicationFrameworkProperties;

        @Bean
        HttpRequestUtility httpRequestUtility() {
            return mock(HttpRequestUtility.class);
        }

        @Bean
        SecurityPrincipalUtility securityPrincipalUtility() {
            return mock(SecurityPrincipalUtility.class);
        }

        @Bean
        AdvancedRequestLoggingFilter advancedRequestLoggingFilter() {
            return new AdvancedRequestLoggingFilter(
                    this.httpRequestUtility(),
                    this.securityPrincipalUtility(),
                    this.applicationFrameworkProperties
            );
        }
    }

    private final HttpRequestUtility httpRequestUtility;
    private final SecurityPrincipalUtility securityPrincipalUtility;
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final AdvancedRequestLoggingFilter componentUnderTest;

    @BeforeEach
    public void beforeEach() {
        reset(
                this.httpRequestUtility,
                this.securityPrincipalUtility
        );
    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(
                this.httpRequestUtility,
                this.securityPrincipalUtility
        );
    }

    @Test
    public void disabledLoggingCachedEndpointTest() throws ServletException, IOException {
        // Arrange
        this.applicationFrameworkProperties.getSecurityJwtConfigs().getLoggingConfigs().setAdvancedRequestLoggingEnabled(false);
        var httpServletRequest = mock(HttpServletRequest.class);
        var httpServletResponse = mock(HttpServletResponse.class);
        var filterChain = mock(FilterChain.class);
        when(this.httpRequestUtility.isCachedEndpoint(any())).thenReturn(true);

        // Act
        this.componentUnderTest.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        // Assert
        verify(this.httpRequestUtility).isCachedEndpoint(any());
        verify(this.httpRequestUtility).cachePayload(any(), anyString());
        verify(filterChain).doFilter(any(CachedBodyHttpServletRequest.class), eq(httpServletResponse));
        verify(httpServletRequest).getInputStream();
        verifyNoMoreInteractions(
                httpServletRequest,
                httpServletResponse,
                filterChain
        );
    }

    @Test
    public void enabledLoggingEmptyPayloadTest() throws ServletException, IOException {
        // Arrange
        this.applicationFrameworkProperties.getSecurityJwtConfigs().getLoggingConfigs().setAdvancedRequestLoggingEnabled(true);
        var httpServletRequest = mock(HttpServletRequest.class);
        var httpServletResponse = mock(HttpServletResponse.class);
        var filterChain = mock(FilterChain.class);
        when(httpServletRequest.getInputStream()).thenReturn(new CachedBodyServletInputStream("".getBytes()));
        when(this.httpRequestUtility.isCachedEndpoint(any())).thenReturn(false);

        // Act
        this.componentUnderTest.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        // Assert
        verify(this.httpRequestUtility).isCachedEndpoint(any());
        verify(this.securityPrincipalUtility).getAuthenticatedUsernameOrUnexpected();
        verify(httpServletRequest).getInputStream();
        verify(httpServletRequest).getMethod();
        verify(httpServletRequest).getServletPath();
        verify(filterChain).doFilter(any(CachedBodyHttpServletRequest.class), eq(httpServletResponse));
        verifyNoMoreInteractions(
                httpServletRequest,
                httpServletResponse,
                filterChain
        );
    }

    @Test
    public void enabledLoggingTest() throws ServletException, IOException {
        // Arrange
        this.applicationFrameworkProperties.getSecurityJwtConfigs().getLoggingConfigs().setAdvancedRequestLoggingEnabled(true);
        var httpServletRequest = mock(HttpServletRequest.class);
        var httpServletResponse = mock(HttpServletResponse.class);
        var filterChain = mock(FilterChain.class);
        when(httpServletRequest.getInputStream()).thenReturn(new CachedBodyServletInputStream("{}".getBytes()));
        when(this.httpRequestUtility.isCachedEndpoint(any())).thenReturn(false);

        // Act
        this.componentUnderTest.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        // Assert
        verify(this.httpRequestUtility).isCachedEndpoint(any());
        verify(this.securityPrincipalUtility).getAuthenticatedUsernameOrUnexpected();
        verify(httpServletRequest).getInputStream();
        verify(httpServletRequest).getMethod();
        verify(httpServletRequest).getServletPath();
        verify(filterChain).doFilter(any(CachedBodyHttpServletRequest.class), eq(httpServletResponse));
        verifyNoMoreInteractions(
                httpServletRequest,
                httpServletResponse,
                filterChain
        );
    }
}
