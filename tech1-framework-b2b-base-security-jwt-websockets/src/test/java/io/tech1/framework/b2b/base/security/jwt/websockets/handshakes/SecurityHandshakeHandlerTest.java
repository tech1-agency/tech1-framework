package io.tech1.framework.b2b.base.security.jwt.websockets.handshakes;

import io.tech1.framework.b2b.base.security.jwt.cookies.CookieProvider;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.services.TokensService;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.exceptions.cookie.*;
import io.tech1.framework.properties.tests.contexts.ApplicationFrameworkPropertiesContext;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.web.socket.WebSocketHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class SecurityHandshakeHandlerTest {

    private static Stream<Arguments> determineUserExceptionTest() {
        return Stream.of(
                Arguments.of(new CookieAccessTokenInvalidException()),
                Arguments.of(new CookieRefreshTokenInvalidException()),
                Arguments.of(new CookieAccessTokenExpiredException(Username.random()))
        );
    }

    @Configuration
    @Import({
            ApplicationFrameworkPropertiesContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {

        @Bean
        TokensService tokenService() {
            return mock(TokensService.class);
        }

        @Bean
        CookieProvider cookieProvider() {
            return mock(CookieProvider.class);
        }

        @Bean
        SecurityHandshakeHandler securityHandshakeHandler() {
            return new SecurityHandshakeHandler(
                    this.tokenService(),
                    this.cookieProvider()
            );
        }
    }

    // Services
    private final TokensService tokensService;
    // Cookie
    private final CookieProvider cookieProvider;

    private final SecurityHandshakeHandler componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.tokensService,
                this.cookieProvider
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.tokensService,
                this.cookieProvider
        );
    }

    @ParameterizedTest
    @MethodSource("determineUserExceptionTest")
    void determineUserExceptionTest(Exception exception) throws CookieAccessTokenInvalidException, CookieRefreshTokenInvalidException, CookieAccessTokenExpiredException, CookieAccessTokenNotFoundException, CookieRefreshTokenNotFoundException, CookieAccessTokenDbNotFoundException {
        // Arrange
        var request = mock(HttpServletRequest.class);
        var serverHttpRequest = mock(ServletServerHttpRequest.class);
        var wsHandler = mock(WebSocketHandler.class);
        Map<String, Object> attributes = new HashMap<>();
        when(serverHttpRequest.getServletRequest()).thenReturn(request);
        var cookieAccessToken = entity(CookieAccessToken.class);
        var cookieRefreshToken = entity(CookieRefreshToken.class);
        when(this.cookieProvider.readJwtAccessToken(any(HttpServletRequest.class))).thenReturn(cookieAccessToken);
        when(this.cookieProvider.readJwtRefreshToken(any(HttpServletRequest.class))).thenReturn(cookieRefreshToken);
        when(this.tokensService.getJwtUserByAccessTokenOrThrow(cookieAccessToken, cookieRefreshToken)).thenThrow(exception);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.determineUser(serverHttpRequest, wsHandler, attributes));

        // Assert
        verify(this.cookieProvider).readJwtAccessToken(any(HttpServletRequest.class));
        verify(this.cookieProvider).readJwtRefreshToken(any(HttpServletRequest.class));
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("WebSocket user not determined");
        verify(this.tokensService).getJwtUserByAccessTokenOrThrow(cookieAccessToken, cookieRefreshToken);
    }

    @Test
    void determineUserTest() throws CookieAccessTokenInvalidException, CookieRefreshTokenInvalidException, CookieAccessTokenExpiredException, CookieAccessTokenNotFoundException, CookieRefreshTokenNotFoundException, CookieAccessTokenDbNotFoundException {
        // Arrange
        var request = mock(HttpServletRequest.class);
        var serverHttpRequest = mock(ServletServerHttpRequest.class);
        var wsHandler = mock(WebSocketHandler.class);
        Map<String, Object> attributes = new HashMap<>();
        var user = entity(JwtUser.class);
        when(serverHttpRequest.getServletRequest()).thenReturn(request);
        var cookieAccessToken = entity(CookieAccessToken.class);
        var cookieRefreshToken = entity(CookieRefreshToken.class);
        when(this.cookieProvider.readJwtAccessToken(any(HttpServletRequest.class))).thenReturn(cookieAccessToken);
        when(this.cookieProvider.readJwtRefreshToken(any(HttpServletRequest.class))).thenReturn(cookieRefreshToken);
        when(this.tokensService.getJwtUserByAccessTokenOrThrow(cookieAccessToken, cookieRefreshToken)).thenReturn(user);

        // Act
        var actual = this.componentUnderTest.determineUser(serverHttpRequest, wsHandler, attributes);

        // Assert
        verify(this.cookieProvider).readJwtAccessToken(any(HttpServletRequest.class));
        verify(this.cookieProvider).readJwtRefreshToken(any(HttpServletRequest.class));
        verify(this.tokensService).getJwtUserByAccessTokenOrThrow(cookieAccessToken, cookieRefreshToken);
        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(user.getUsername());
    }
}
