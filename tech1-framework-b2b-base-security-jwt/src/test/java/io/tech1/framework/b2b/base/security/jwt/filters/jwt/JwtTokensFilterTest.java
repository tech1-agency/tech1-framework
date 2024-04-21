package io.tech1.framework.b2b.base.security.jwt.filters.jwt;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.sessions.Session;
import io.tech1.framework.b2b.base.security.jwt.filters.jwt_extension.JwtTokensFilterExtension;
import io.tech1.framework.b2b.base.security.jwt.handlers.exceptions.JwtAccessDeniedExceptionHandler;
import io.tech1.framework.b2b.base.security.jwt.services.TokensService;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.b2b.base.security.jwt.tokens.facade.TokensProvider;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.exceptions.tokens.*;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class JwtTokensFilterTest {

    private static Stream<Arguments> clearCookieTest() {
        return Stream.of(
                Arguments.of(new AccessTokenInvalidException()),
                Arguments.of(new RefreshTokenInvalidException()),
                Arguments.of(new AccessTokenDbNotFoundException(Username.testsHardcoded()))
        );
    }

    @Configuration
    static class ContextConfiguration {
        @Bean
        SessionRegistry sessionRegistry() {
            return mock(SessionRegistry.class);
        }

        @Bean
        TokensService tokenService() {
            return mock(TokensService.class);
        }

        @Bean
        TokensProvider cookieProvider() {
            return mock(TokensProvider.class);
        }

        @Bean
        JwtTokensFilterExtension jwtTokenFilterExtension() {
            return mock(JwtTokensFilterExtension.class);
        }

        @Bean
        JwtAccessDeniedExceptionHandler jwtAccessDeniedExceptionHandler() {
            return mock(JwtAccessDeniedExceptionHandler.class);
        }

        @Bean
        JwtTokensFilter jwtAccessTokenFilter() {
            return new JwtTokensFilter(
                    this.sessionRegistry(),
                    this.tokenService(),
                    this.cookieProvider(),
                    this.jwtTokenFilterExtension(),
                    this.jwtAccessDeniedExceptionHandler()
            );
        }
    }

    // Session
    private final SessionRegistry sessionRegistry;
    // Services
    private final TokensService tokensService;
    // Tokens
    private final TokensProvider tokensProvider;
    // Extension
    private final JwtTokensFilterExtension jwtTokensFilterExtension;
    // Handlers
    private final JwtAccessDeniedExceptionHandler jwtAccessDeniedExceptionHandler;

    private final JwtTokensFilter componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.sessionRegistry,
                this.tokensService,
                this.tokensProvider,
                this.jwtTokensFilterExtension,
                this.jwtAccessDeniedExceptionHandler
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.sessionRegistry,
                this.tokensService,
                this.tokensProvider,
                this.jwtTokensFilterExtension,
                this.jwtAccessDeniedExceptionHandler
        );
    }

    @Test
    void accessTokenNotFoundTest() throws Exception {
        // Arrange
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var filterChain = mock(FilterChain.class);
        when(this.tokensProvider.readRequestAccessToken(any(HttpServletRequest.class))).thenThrow(new AccessTokenNotFoundException());

        // Act
        this.componentUnderTest.doFilterInternal(request, response, filterChain);

        // Assert
        verify(this.tokensProvider).readRequestAccessToken(any(HttpServletRequest.class));
        verify(filterChain).doFilter(request, response);
        verifyNoMoreInteractions(
                request,
                response,
                filterChain
        );
    }

    @Test
    void accessTokenExpiredTest() throws Exception {
        // Arrange
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var filterChain = mock(FilterChain.class);
        var requestAccessToken = RequestAccessToken.random();
        var requestRefreshToken = RequestRefreshToken.random();
        when(this.tokensProvider.readRequestAccessToken(any(HttpServletRequest.class))).thenReturn(requestAccessToken);
        when(this.tokensProvider.readRequestRefreshToken(any(HttpServletRequest.class))).thenReturn(requestRefreshToken);
        when(this.tokensService.getJwtUserByAccessTokenOrThrow(requestAccessToken, requestRefreshToken)).thenThrow(new AccessTokenExpiredException(Username.testsHardcoded()));

        // Act
        this.componentUnderTest.doFilterInternal(request, response, filterChain);

        // Assert
        verify(this.tokensProvider).readRequestAccessToken(any(HttpServletRequest.class));
        verify(this.tokensProvider).readRequestRefreshToken(any(HttpServletRequest.class));
        verify(this.tokensService).getJwtUserByAccessTokenOrThrow(requestAccessToken, requestRefreshToken);
        verify(filterChain).doFilter(request, response);
        verifyNoMoreInteractions(
                request,
                response,
                filterChain
        );
    }

    @Test
    void refreshTokenNotFoundTest() throws Exception {
        // Arrange
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var filterChain = mock(FilterChain.class);
        var requestAccessToken = RequestAccessToken.random();
        when(this.tokensProvider.readRequestAccessToken(any(HttpServletRequest.class))).thenReturn(requestAccessToken);
        when(this.tokensProvider.readRequestRefreshToken(any(HttpServletRequest.class))).thenThrow(new RefreshTokenNotFoundException());

        // Act
        this.componentUnderTest.doFilterInternal(request, response, filterChain);

        // Assert
        verify(this.tokensProvider).readRequestAccessToken(any(HttpServletRequest.class));
        verify(this.tokensProvider).readRequestRefreshToken(any(HttpServletRequest.class));
        verify(this.tokensProvider).clearTokens(response);
        verify(response).sendError(HttpStatus.UNAUTHORIZED.value());
        verifyNoMoreInteractions(
                request,
                response,
                filterChain
        );
    }

    @ParameterizedTest
    @MethodSource("clearCookieTest")
    void clearCookieTest(Exception exception) throws Exception {
        // Arrange
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var filterChain = mock(FilterChain.class);
        var requestAccessToken = RequestAccessToken.random();
        var requestRefreshToken = RequestRefreshToken.random();
        when(this.tokensProvider.readRequestAccessToken(any(HttpServletRequest.class))).thenReturn(requestAccessToken);
        when(this.tokensProvider.readRequestRefreshToken(any(HttpServletRequest.class))).thenReturn(requestRefreshToken);
        when(this.tokensService.getJwtUserByAccessTokenOrThrow(requestAccessToken, requestRefreshToken)).thenThrow(exception);

        // Act
        this.componentUnderTest.doFilterInternal(request, response, filterChain);

        // Assert
        verify(this.tokensProvider).readRequestAccessToken(any(HttpServletRequest.class));
        verify(this.tokensProvider).readRequestRefreshToken(any(HttpServletRequest.class));
        verify(this.tokensService).getJwtUserByAccessTokenOrThrow(requestAccessToken, requestRefreshToken);
        verify(this.tokensProvider).clearTokens(response);
        verify(response).sendError(HttpStatus.UNAUTHORIZED.value());
        verifyNoMoreInteractions(
                request,
                response,
                filterChain
        );
    }

    @Test
    void accessTokenValidTest() throws Exception {
        // Arrange
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var filterChain = mock(FilterChain.class);
        var user = entity(JwtUser.class);
        var requestAccessToken = RequestAccessToken.random();
        var requestRefreshToken = RequestRefreshToken.random();
        when(this.tokensProvider.readRequestAccessToken(any(HttpServletRequest.class))).thenReturn(requestAccessToken);
        when(this.tokensProvider.readRequestRefreshToken(any(HttpServletRequest.class))).thenReturn(requestRefreshToken);
        when(this.tokensService.getJwtUserByAccessTokenOrThrow(requestAccessToken, requestRefreshToken)).thenReturn(user);

        // Act
        this.componentUnderTest.doFilterInternal(request, response, filterChain);

        // Assert
        verify(this.tokensProvider).readRequestAccessToken(any(HttpServletRequest.class));
        verify(this.tokensProvider).readRequestRefreshToken(any(HttpServletRequest.class));
        verify(this.tokensService).getJwtUserByAccessTokenOrThrow(requestAccessToken, requestRefreshToken);
        // no verifications on static SecurityContextHolder
        verify(this.sessionRegistry).register(new Session(user.username(), requestAccessToken.getJwtAccessToken(), requestRefreshToken.getJwtRefreshToken()));
        verify(this.jwtTokensFilterExtension).doFilter(request);
        verify(filterChain).doFilter(request, response);
        verifyNoMoreInteractions(
                request,
                response,
                filterChain
        );
    }

    @Test
    void tokenExtensionUnauthorizedExceptionTest() throws Exception {
        // Arrange
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var filterChain = mock(FilterChain.class);
        var user = entity(JwtUser.class);
        var requestAccessToken = RequestAccessToken.random();
        var requestRefreshToken = RequestRefreshToken.random();
        when(this.tokensProvider.readRequestAccessToken(any(HttpServletRequest.class))).thenReturn(requestAccessToken);
        when(this.tokensProvider.readRequestRefreshToken(any(HttpServletRequest.class))).thenReturn(requestRefreshToken);
        when(this.tokensService.getJwtUserByAccessTokenOrThrow(requestAccessToken, requestRefreshToken)).thenReturn(user);
        doThrow(new TokenExtensionUnauthorizedException(randomString())).when(this.jwtTokensFilterExtension).doFilter(request);

        // Act
        this.componentUnderTest.doFilterInternal(request, response, filterChain);

        // Assert
        verify(this.tokensProvider).readRequestAccessToken(any(HttpServletRequest.class));
        verify(this.tokensProvider).readRequestRefreshToken(any(HttpServletRequest.class));
        verify(this.tokensService).getJwtUserByAccessTokenOrThrow(requestAccessToken, requestRefreshToken);
        // no verifications on static SecurityContextHolder
        verify(this.sessionRegistry).register(new Session(user.username(), requestAccessToken.getJwtAccessToken(), requestRefreshToken.getJwtRefreshToken()));
        verify(this.jwtTokensFilterExtension).doFilter(request);
        verify(this.tokensProvider).clearTokens(response);
        verify(response).sendError(HttpStatus.UNAUTHORIZED.value());
        verifyNoMoreInteractions(
                request,
                response,
                filterChain
        );
    }

    @Test
    void tokenExtensionAccessDeniedExceptionTest() throws Exception {
        // Arrange
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var filterChain = mock(FilterChain.class);
        var user = entity(JwtUser.class);
        var requestAccessToken = RequestAccessToken.random();
        var requestRefreshToken = RequestRefreshToken.random();
        when(this.tokensProvider.readRequestAccessToken(any(HttpServletRequest.class))).thenReturn(requestAccessToken);
        when(this.tokensProvider.readRequestRefreshToken(any(HttpServletRequest.class))).thenReturn(requestRefreshToken);
        when(this.tokensService.getJwtUserByAccessTokenOrThrow(requestAccessToken, requestRefreshToken)).thenReturn(user);
        var exception = new TokenExtensionAccessDeniedException(randomString());
        doThrow(exception).when(this.jwtTokensFilterExtension).doFilter(request);

        // Act
        this.componentUnderTest.doFilterInternal(request, response, filterChain);

        // Assert
        verify(this.tokensProvider).readRequestAccessToken(any(HttpServletRequest.class));
        verify(this.tokensProvider).readRequestRefreshToken(any(HttpServletRequest.class));
        verify(this.tokensService).getJwtUserByAccessTokenOrThrow(requestAccessToken, requestRefreshToken);
        // no verifications on static SecurityContextHolder
        verify(this.sessionRegistry).register(new Session(user.username(), requestAccessToken.getJwtAccessToken(), requestRefreshToken.getJwtRefreshToken()));
        verify(this.jwtTokensFilterExtension).doFilter(request);
        verify(this.tokensProvider).clearTokens(response);
        var exceptionAC = ArgumentCaptor.forClass(AccessDeniedException.class);
        verify(this.jwtAccessDeniedExceptionHandler).handle(eq(request), eq(response), exceptionAC.capture());
        assertThat(exceptionAC.getValue().getMessage()).isEqualTo(exception.getMessage());
        verifyNoMoreInteractions(
                request,
                response,
                filterChain
        );
    }
}
