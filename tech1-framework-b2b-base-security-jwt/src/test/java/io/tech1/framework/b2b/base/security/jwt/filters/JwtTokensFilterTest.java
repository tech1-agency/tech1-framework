package io.tech1.framework.b2b.base.security.jwt.filters;

import io.tech1.framework.b2b.base.security.jwt.tokens.TokensProvider;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.domain.sessions.Session;
import io.tech1.framework.b2b.base.security.jwt.services.TokensService;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
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
        JwtTokensFilter jwtAccessTokenFilter() {
            return new JwtTokensFilter(
                    this.sessionRegistry(),
                    this.tokenService(),
                    this.cookieProvider()
            );
        }
    }

    private final SessionRegistry sessionRegistry;
    private final TokensService tokensService;
    private final TokensProvider tokensProvider;

    private final JwtTokensFilter componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.sessionRegistry,
                this.tokensService,
                this.tokensProvider
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.sessionRegistry,
                this.tokensService,
                this.tokensProvider
        );
    }

    @Test
    void accessTokenNotFoundTest() throws Exception {
        // Arrange
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var filterChain = mock(FilterChain.class);
        when(this.tokensProvider.readJwtAccessToken(any(HttpServletRequest.class))).thenThrow(new AccessTokenNotFoundException());

        // Act
        this.componentUnderTest.doFilterInternal(request, response, filterChain);

        // Assert
        verify(this.tokensProvider).readJwtAccessToken(any(HttpServletRequest.class));
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
        when(this.tokensProvider.readJwtAccessToken(any(HttpServletRequest.class))).thenReturn(requestAccessToken);
        when(this.tokensProvider.readJwtRefreshToken(any(HttpServletRequest.class))).thenReturn(requestRefreshToken);
        when(this.tokensService.getJwtUserByAccessTokenOrThrow(requestAccessToken, requestRefreshToken)).thenThrow(new AccessTokenExpiredException(Username.testsHardcoded()));

        // Act
        this.componentUnderTest.doFilterInternal(request, response, filterChain);

        // Assert
        verify(this.tokensProvider).readJwtAccessToken(any(HttpServletRequest.class));
        verify(this.tokensProvider).readJwtRefreshToken(any(HttpServletRequest.class));
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
        when(this.tokensProvider.readJwtAccessToken(any(HttpServletRequest.class))).thenReturn(requestAccessToken);
        when(this.tokensProvider.readJwtRefreshToken(any(HttpServletRequest.class))).thenThrow(new RefreshTokenNotFoundException());

        // Act
        this.componentUnderTest.doFilterInternal(request, response, filterChain);

        // Assert
        verify(this.tokensProvider).readJwtAccessToken(any(HttpServletRequest.class));
        verify(this.tokensProvider).readJwtRefreshToken(any(HttpServletRequest.class));
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
        when(this.tokensProvider.readJwtAccessToken(any(HttpServletRequest.class))).thenReturn(requestAccessToken);
        when(this.tokensProvider.readJwtRefreshToken(any(HttpServletRequest.class))).thenReturn(requestRefreshToken);
        when(this.tokensService.getJwtUserByAccessTokenOrThrow(requestAccessToken, requestRefreshToken)).thenThrow(exception);

        // Act
        this.componentUnderTest.doFilterInternal(request, response, filterChain);

        // Assert
        verify(this.tokensProvider).readJwtAccessToken(any(HttpServletRequest.class));
        verify(this.tokensProvider).readJwtRefreshToken(any(HttpServletRequest.class));
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
        when(this.tokensProvider.readJwtAccessToken(any(HttpServletRequest.class))).thenReturn(requestAccessToken);
        when(this.tokensProvider.readJwtRefreshToken(any(HttpServletRequest.class))).thenReturn(requestRefreshToken);
        when(this.tokensService.getJwtUserByAccessTokenOrThrow(requestAccessToken, requestRefreshToken)).thenReturn(user);

        // Act
        this.componentUnderTest.doFilterInternal(request, response, filterChain);

        // Assert
        verify(this.tokensProvider).readJwtAccessToken(any(HttpServletRequest.class));
        verify(this.tokensProvider).readJwtRefreshToken(any(HttpServletRequest.class));
        verify(this.tokensService).getJwtUserByAccessTokenOrThrow(requestAccessToken, requestRefreshToken);
        // no verifications on static SecurityContextHolder
        verify(this.sessionRegistry).register(new Session(user.username(), requestAccessToken.getJwtAccessToken(), requestRefreshToken.getJwtRefreshToken()));
        verify(filterChain).doFilter(request, response);
        verifyNoMoreInteractions(
                request,
                response,
                filterChain
        );
    }
}
