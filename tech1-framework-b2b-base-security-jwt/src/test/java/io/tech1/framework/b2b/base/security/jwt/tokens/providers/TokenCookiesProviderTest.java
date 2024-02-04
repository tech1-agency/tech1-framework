package io.tech1.framework.b2b.base.security.jwt.tokens.providers;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.exceptions.tokens.AccessTokenNotFoundException;
import io.tech1.framework.domain.exceptions.tokens.RefreshTokenNotFoundException;
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

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class TokenCookiesProviderTest {

    @Configuration
    @Import({
            ApplicationFrameworkPropertiesContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final ApplicationFrameworkProperties applicationFrameworkProperties;

        @Bean
        TokenCookiesProvider tokensCookiesProvider() {
            return new TokenCookiesProvider(
                    this.applicationFrameworkProperties
            );
        }
    }

    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    private final TokenCookiesProvider componentUnderTest;

    @Test
    void createResponseAccessToken() {
        // Arrange
        var jwtAccessToken = JwtAccessToken.random();
        var response = mock(HttpServletResponse.class);

        var cookiesConfigs = this.applicationFrameworkProperties.getSecurityJwtConfigs().getCookiesConfigs();
        var accessToken = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getAccessToken();
        var maxAge = accessToken.getExpiration().getTimeAmount().toSeconds() - cookiesConfigs.getJwtAccessTokenCookieCreationLatency().getTimeAmount().toSeconds();

        // Act
        this.componentUnderTest.createResponseAccessToken(jwtAccessToken, response);

        // Assert
        var cookieAC = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieAC.capture());
        var cookie = cookieAC.getValue();
        assertThat(cookie.getName()).isEqualTo(accessToken.getCookieKey());
        assertThat(cookie.getValue()).isEqualTo(jwtAccessToken.value());
        assertThat(cookie.getDomain()).isEqualTo(cookiesConfigs.getDomain());
        assertThat(cookie.isHttpOnly()).isTrue();
        assertThat(cookie.getMaxAge()).isEqualTo(maxAge);
        verifyNoMoreInteractions(response);
    }

    @Test
    void createResponseRefreshToken() {
        // Arrange
        var refreshAccessToken = JwtRefreshToken.random();
        var response = mock(HttpServletResponse.class);

        var cookiesConfigs = this.applicationFrameworkProperties.getSecurityJwtConfigs().getCookiesConfigs();
        var refreshToken = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getRefreshToken();

        // Act
        this.componentUnderTest.createResponseRefreshToken(refreshAccessToken, response);

        // Assert
        var cookieAC = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieAC.capture());
        var cookie = cookieAC.getValue();
        assertThat(cookie.getName()).isEqualTo(refreshToken.getCookieKey());
        assertThat(cookie.getValue()).isEqualTo(refreshAccessToken.value());
        assertThat(cookie.getDomain()).isEqualTo(cookiesConfigs.getDomain());
        assertThat(cookie.isHttpOnly()).isTrue();
        assertThat(cookie.getMaxAge()).isEqualTo(refreshToken.getExpiration().getTimeAmount().toSeconds());
        verifyNoMoreInteractions(response);
    }

    @Test
    void readRequestAccessToken() throws AccessTokenNotFoundException {
        // Arrange
        var accessToken = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getAccessToken();
        var cookie = mock(Cookie.class);
        when(cookie.getName()).thenReturn(accessToken.getCookieKey());
        var request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[] { cookie });

        // Act
        this.componentUnderTest.readRequestAccessToken(request);

        // Assert
        verify(request).getCookies();
        assertThat(request.getCookies()).hasSize(1);
    }

    @Test
    void readRequestAccessTokenThrow() {
        // Arrange
        var request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[] { });

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.readRequestAccessToken(request));

        // Assert
        verify(request).getCookies();
        assertThat(request.getCookies()).isEmpty();
        assertThat(throwable)
                .isInstanceOf(AccessTokenNotFoundException.class)
                .hasMessageContaining("JWT access token not found");
    }

    @Test
    void readRequestRefreshToken() throws RefreshTokenNotFoundException {
        // Arrange
        var refreshToken = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getRefreshToken();
        var cookie = mock(Cookie.class);
        when(cookie.getName()).thenReturn(refreshToken.getCookieKey());
        var request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[] { cookie });

        // Act
        this.componentUnderTest.readRequestRefreshToken(request);

        // Assert
        verify(request).getCookies();
        assertThat(request.getCookies()).hasSize(1);
    }

    @Test
    void readRequestRefreshTokenThrow() {
        // Arrange
        var request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[] { });

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.readRequestRefreshToken(request));

        // Assert
        verify(request).getCookies();
        assertThat(request.getCookies()).isEmpty();
        assertThat(throwable)
                .isInstanceOf(RefreshTokenNotFoundException.class)
                .hasMessageContaining("JWT refresh token not found");
    }

    @Test
    void clearTokens() {
        // Arrange
        var response = mock(HttpServletResponse.class);
        var domain = this.applicationFrameworkProperties.getSecurityJwtConfigs().getCookiesConfigs().getDomain();
        var accessToken = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getAccessToken();
        var refreshToken = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getRefreshToken();

        // Act
        this.componentUnderTest.clearTokens(response);

        // Assert
        var cookiesAC = ArgumentCaptor.forClass(Cookie.class);
        verify(response, times(2)).addCookie(cookiesAC.capture());
        var cookies = cookiesAC.getAllValues();
        var cookieKeys = Stream.of(accessToken.getCookieKey(), refreshToken.getCookieKey()).collect(Collectors.toSet());
        cookies.forEach(cookie -> {
            assertThat(cookieKeys).contains(cookie.getName());
            assertThat(domain).isEqualTo(cookie.getDomain());
        });
        verifyNoMoreInteractions(response);
    }
}
