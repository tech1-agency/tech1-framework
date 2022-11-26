package io.tech1.framework.b2b.mongodb.security.jwt.cookies.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.cookies.CookieProvider;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.exceptions.cookie.CookieAccessTokenNotFoundException;
import io.tech1.framework.domain.exceptions.cookie.CookieRefreshTokenNotFoundException;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import io.tech1.framework.properties.tests.contexts.ApplicationFrameworkPropertiesContext;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CookieProviderImplTest {

    @Configuration
    @Import({
            ApplicationFrameworkPropertiesContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final ApplicationFrameworkProperties applicationFrameworkProperties;

        @Bean
        CookieProvider cookieProvider() {
            return new CookieProviderImpl(
                    this.applicationFrameworkProperties
            );
        }
    }

    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final CookieProvider componentUnderTest;

    @Test
    public void createJwtAccessCookie() {
        // Arrange
        var jwtAccessToken = new JwtAccessToken(randomString());
        var httpServletResponse = mock(HttpServletResponse.class);

        var cookiesConfigs = this.applicationFrameworkProperties.getSecurityJwtConfigs().getCookiesConfigs();
        var accessToken = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getAccessToken();
        var maxAge = accessToken.getExpiration().getTimeAmount().toSeconds() - cookiesConfigs.getJwtAccessTokenCookieCreationLatency().getTimeAmount().toSeconds();

        // Act
        this.componentUnderTest.createJwtAccessCookie(jwtAccessToken, httpServletResponse);

        // Assert
        var cookieAC = ArgumentCaptor.forClass(Cookie.class);
        verify(httpServletResponse).addCookie(cookieAC.capture());
        var cookie = cookieAC.getValue();
        assertThat(cookie.getName()).isEqualTo(accessToken.getCookieKey());
        assertThat(cookie.getValue()).isEqualTo(jwtAccessToken.getValue());
        assertThat(cookie.getDomain()).isEqualTo(cookiesConfigs.getDomain());
        assertThat(cookie.isHttpOnly()).isEqualTo(true);
        assertThat(cookie.getMaxAge()).isEqualTo(maxAge);
        verifyNoMoreInteractions(httpServletResponse);
    }

    @Test
    public void createJwtRefreshCookie() {
        // Arrange
        var refreshAccessToken = entity(JwtRefreshToken.class);
        var httpServletResponse = mock(HttpServletResponse.class);

        var cookiesConfigs = this.applicationFrameworkProperties.getSecurityJwtConfigs().getCookiesConfigs();
        var refreshToken = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getRefreshToken();

        // Act
        this.componentUnderTest.createJwtRefreshCookie(refreshAccessToken, httpServletResponse);

        // Assert
        var cookieAC = ArgumentCaptor.forClass(Cookie.class);
        verify(httpServletResponse).addCookie(cookieAC.capture());
        var cookie = cookieAC.getValue();
        assertThat(cookie.getName()).isEqualTo(refreshToken.getCookieKey());
        assertThat(cookie.getValue()).isEqualTo(refreshAccessToken.getValue());
        assertThat(cookie.getDomain()).isEqualTo(cookiesConfigs.getDomain());
        assertThat(cookie.isHttpOnly()).isEqualTo(true);
        assertThat(cookie.getMaxAge()).isEqualTo(refreshToken.getExpiration().getTimeAmount().toSeconds());
        verifyNoMoreInteractions(httpServletResponse);
    }

    @Test
    public void readJwtAccessToken() throws CookieAccessTokenNotFoundException {
        // Arrange
        var accessToken = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getAccessToken();
        var cookie = mock(Cookie.class);
        when(cookie.getName()).thenReturn(accessToken.getCookieKey());
        var httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getCookies()).thenReturn(new Cookie[] { cookie });

        // Act
        this.componentUnderTest.readJwtAccessToken(httpServletRequest);

        // Assert
        verify(httpServletRequest).getCookies();
        assertThat(httpServletRequest.getCookies()).hasSize(1);
    }

    @Test
    public void readJwtAccessTokenThrow() {
        // Arrange
        var httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getCookies()).thenReturn(new Cookie[] { });

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.readJwtAccessToken(httpServletRequest));

        // Assert
        verify(httpServletRequest).getCookies();
        assertThat(httpServletRequest.getCookies()).hasSize(0);
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(CookieAccessTokenNotFoundException.class);
        assertThat(throwable).hasMessageContaining("JWT access token not found");
    }

    @Test
    public void readJwtRefreshToken() throws CookieRefreshTokenNotFoundException {
        // Arrange
        var refreshToken = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getRefreshToken();
        var cookie = mock(Cookie.class);
        when(cookie.getName()).thenReturn(refreshToken.getCookieKey());
        var httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getCookies()).thenReturn(new Cookie[] { cookie });

        // Act
        this.componentUnderTest.readJwtRefreshToken(httpServletRequest);

        // Assert
        verify(httpServletRequest).getCookies();
        assertThat(httpServletRequest.getCookies()).hasSize(1);
    }

    @Test
    public void readJwtRefreshTokenThrow() {
        // Arrange
        var httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getCookies()).thenReturn(new Cookie[] { });

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.readJwtRefreshToken(httpServletRequest));

        // Assert
        verify(httpServletRequest).getCookies();
        assertThat(httpServletRequest.getCookies()).hasSize(0);
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(CookieRefreshTokenNotFoundException.class);
        assertThat(throwable).hasMessageContaining("JWT refresh token not found");
    }

    @Test
    public void clearCookies() {
        // Arrange
        var httpServletResponse = mock(HttpServletResponse.class);
        var domain = this.applicationFrameworkProperties.getSecurityJwtConfigs().getCookiesConfigs().getDomain();
        var accessToken = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getAccessToken();
        var refreshToken = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getRefreshToken();

        // Act
        this.componentUnderTest.clearCookies(httpServletResponse);

        // Assert
        var cookiesAC = ArgumentCaptor.forClass(Cookie.class);
        verify(httpServletResponse, times(2)).addCookie(cookiesAC.capture());
        var cookies = cookiesAC.getAllValues();
        var cookieKeys = Stream.of(accessToken.getCookieKey(), refreshToken.getCookieKey()).collect(Collectors.toSet());
        cookies.forEach(cookie -> {
            assertThat(cookieKeys).contains(cookie.getName());
            assertThat(domain).isEqualTo(cookie.getDomain());
        });
        verifyNoMoreInteractions(httpServletResponse);
    }
}
