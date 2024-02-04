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
import org.springframework.beans.factory.annotation.Qualifier;
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
class TokensCookieProviderImplTest {

    @Configuration
    @Import({
            ApplicationFrameworkPropertiesContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final ApplicationFrameworkProperties applicationFrameworkProperties;

        @Qualifier("tokensCookiesProvider")
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
        var jwtAccessToken = new JwtAccessToken(randomString());
        var httpServletResponse = mock(HttpServletResponse.class);

        var cookiesConfigs = this.applicationFrameworkProperties.getSecurityJwtConfigs().getCookiesConfigs();
        var accessToken = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getAccessToken();
        var maxAge = accessToken.getExpiration().getTimeAmount().toSeconds() - cookiesConfigs.getJwtAccessTokenCookieCreationLatency().getTimeAmount().toSeconds();

        // Act
        this.componentUnderTest.createResponseAccessToken(jwtAccessToken, httpServletResponse);

        // Assert
        var cookieAC = ArgumentCaptor.forClass(Cookie.class);
        verify(httpServletResponse).addCookie(cookieAC.capture());
        var cookie = cookieAC.getValue();
        assertThat(cookie.getName()).isEqualTo(accessToken.getCookieKey());
        assertThat(cookie.getValue()).isEqualTo(jwtAccessToken.value());
        assertThat(cookie.getDomain()).isEqualTo(cookiesConfigs.getDomain());
        assertThat(cookie.isHttpOnly()).isTrue();
        assertThat(cookie.getMaxAge()).isEqualTo(maxAge);
        verifyNoMoreInteractions(httpServletResponse);
    }

    @Test
    void createResponseRefreshToken() {
        // Arrange
        var refreshAccessToken = JwtRefreshToken.random();
        var httpServletResponse = mock(HttpServletResponse.class);

        var cookiesConfigs = this.applicationFrameworkProperties.getSecurityJwtConfigs().getCookiesConfigs();
        var refreshToken = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getRefreshToken();

        // Act
        this.componentUnderTest.createResponseRefreshToken(refreshAccessToken, httpServletResponse);

        // Assert
        var cookieAC = ArgumentCaptor.forClass(Cookie.class);
        verify(httpServletResponse).addCookie(cookieAC.capture());
        var cookie = cookieAC.getValue();
        assertThat(cookie.getName()).isEqualTo(refreshToken.getCookieKey());
        assertThat(cookie.getValue()).isEqualTo(refreshAccessToken.value());
        assertThat(cookie.getDomain()).isEqualTo(cookiesConfigs.getDomain());
        assertThat(cookie.isHttpOnly()).isTrue();
        assertThat(cookie.getMaxAge()).isEqualTo(refreshToken.getExpiration().getTimeAmount().toSeconds());
        verifyNoMoreInteractions(httpServletResponse);
    }

    @Test
    void readRequestAccessToken() throws AccessTokenNotFoundException {
        // Arrange
        var accessToken = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getAccessToken();
        var cookie = mock(Cookie.class);
        when(cookie.getName()).thenReturn(accessToken.getCookieKey());
        var httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getCookies()).thenReturn(new Cookie[] { cookie });

        // Act
        this.componentUnderTest.readRequestAccessToken(httpServletRequest);

        // Assert
        verify(httpServletRequest).getCookies();
        assertThat(httpServletRequest.getCookies()).hasSize(1);
    }

    @Test
    void readRequestAccessTokenThrow() {
        // Arrange
        var httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getCookies()).thenReturn(new Cookie[] { });

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.readRequestAccessToken(httpServletRequest));

        // Assert
        verify(httpServletRequest).getCookies();
        assertThat(httpServletRequest.getCookies()).isEmpty();;
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
        var httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getCookies()).thenReturn(new Cookie[] { cookie });

        // Act
        this.componentUnderTest.readRequestRefreshToken(httpServletRequest);

        // Assert
        verify(httpServletRequest).getCookies();
        assertThat(httpServletRequest.getCookies()).hasSize(1);
    }

    @Test
    void readRequestRefreshTokenThrow() {
        // Arrange
        var httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getCookies()).thenReturn(new Cookie[] { });

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.readRequestRefreshToken(httpServletRequest));

        // Assert
        verify(httpServletRequest).getCookies();
        assertThat(httpServletRequest.getCookies()).isEmpty();
        assertThat(throwable)
                .isInstanceOf(RefreshTokenNotFoundException.class)
                .hasMessageContaining("JWT refresh token not found");
    }

    @Test
    void clearTokens() {
        // Arrange
        var httpServletResponse = mock(HttpServletResponse.class);
        var domain = this.applicationFrameworkProperties.getSecurityJwtConfigs().getCookiesConfigs().getDomain();
        var accessToken = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getAccessToken();
        var refreshToken = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getRefreshToken();

        // Act
        this.componentUnderTest.clearTokens(httpServletResponse);

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
