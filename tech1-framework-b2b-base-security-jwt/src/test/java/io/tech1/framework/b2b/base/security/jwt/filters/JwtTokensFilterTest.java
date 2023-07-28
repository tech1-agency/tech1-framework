package io.tech1.framework.b2b.base.security.jwt.filters;

import io.tech1.framework.b2b.base.security.jwt.cookies.CookieProvider;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.domain.sessions.Session;
import io.tech1.framework.b2b.base.security.jwt.services.TokenService;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.domain.exceptions.cookie.CookieAccessTokenExpiredException;
import io.tech1.framework.domain.exceptions.cookie.CookieAccessTokenInvalidException;
import io.tech1.framework.domain.exceptions.cookie.CookieRefreshTokenInvalidException;
import io.tech1.framework.domain.tuples.Tuple2;
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
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomUsername;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class JwtTokensFilterTest {

    private static Stream<Arguments> accessTokenCookieRequiredTest() {
        return Stream.of(
                Arguments.of(new CookieAccessTokenExpiredException(randomUsername()))
        );
    }

    private static Stream<Arguments> clearCookieTest() {
        return Stream.of(
                Arguments.of(new CookieAccessTokenInvalidException()),
                Arguments.of(new CookieRefreshTokenInvalidException())
        );
    }

    @Configuration
    static class ContextConfiguration {
        @Bean
        SessionRegistry sessionRegistry() {
            return mock(SessionRegistry.class);
        }

        @Bean
        TokenService tokenService() {
            return mock(TokenService.class);
        }

        @Bean
        CookieProvider cookieProvider() {
            return mock(CookieProvider.class);
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
    private final TokenService tokenService;
    private final CookieProvider cookieProvider;

    private final JwtTokensFilter componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.sessionRegistry,
                this.tokenService,
                this.cookieProvider
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.sessionRegistry,
                this.tokenService,
                this.cookieProvider
        );
    }

    @ParameterizedTest
    @MethodSource("accessTokenCookieRequiredTest")
    void accessTokenCookieRequiredTest(Exception exception) throws Exception {
        // Arrange
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var filterChain = mock(FilterChain.class);
        var cookieAccessToken = entity(CookieAccessToken.class);
        var cookieRefreshToken = entity(CookieRefreshToken.class);
        when(this.cookieProvider.readJwtAccessToken(any(HttpServletRequest.class))).thenReturn(cookieAccessToken);
        when(this.cookieProvider.readJwtRefreshToken(any(HttpServletRequest.class))).thenReturn(cookieRefreshToken);
        when(this.tokenService.getJwtUserByAccessTokenOrThrow(cookieAccessToken, cookieRefreshToken)).thenThrow(exception);

        // Act
        this.componentUnderTest.doFilterInternal(request, response, filterChain);

        // Assert
        verify(this.cookieProvider).readJwtAccessToken(any(HttpServletRequest.class));
        verify(this.cookieProvider).readJwtRefreshToken(any(HttpServletRequest.class));
        verify(this.tokenService).getJwtUserByAccessTokenOrThrow(cookieAccessToken, cookieRefreshToken);
        verify(filterChain).doFilter(request, response);
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
        var cookieAccessToken = entity(CookieAccessToken.class);
        var cookieRefreshToken = entity(CookieRefreshToken.class);
        when(this.cookieProvider.readJwtAccessToken(any(HttpServletRequest.class))).thenReturn(cookieAccessToken);
        when(this.cookieProvider.readJwtRefreshToken(any(HttpServletRequest.class))).thenReturn(cookieRefreshToken);
        when(this.tokenService.getJwtUserByAccessTokenOrThrow(cookieAccessToken, cookieRefreshToken)).thenThrow(exception);

        // Act
        this.componentUnderTest.doFilterInternal(request, response, filterChain);

        // Assert
        verify(this.cookieProvider).readJwtAccessToken(any(HttpServletRequest.class));
        verify(this.cookieProvider).readJwtRefreshToken(any(HttpServletRequest.class));
        verify(this.tokenService).getJwtUserByAccessTokenOrThrow(cookieAccessToken, cookieRefreshToken);
        verify(this.cookieProvider).clearCookies(response);
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
        var jwtRefreshToken = entity(JwtRefreshToken.class);
        var jwtUser = entity(JwtUser.class);
        var cookieAccessToken = entity(CookieAccessToken.class);
        var cookieRefreshToken = entity(CookieRefreshToken.class);
        when(this.cookieProvider.readJwtAccessToken(any(HttpServletRequest.class))).thenReturn(cookieAccessToken);
        when(this.cookieProvider.readJwtRefreshToken(any(HttpServletRequest.class))).thenReturn(cookieRefreshToken);
        when(this.tokenService.getJwtUserByAccessTokenOrThrow(cookieAccessToken, cookieRefreshToken)).thenReturn(new Tuple2<>(jwtUser, jwtRefreshToken));

        // Act
        this.componentUnderTest.doFilterInternal(request, response, filterChain);

        // Assert
        verify(this.cookieProvider).readJwtAccessToken(any(HttpServletRequest.class));
        verify(this.cookieProvider).readJwtRefreshToken(any(HttpServletRequest.class));
        verify(this.tokenService).getJwtUserByAccessTokenOrThrow(cookieAccessToken, cookieRefreshToken);
        // WARNING: no verifications on static SecurityContextHolder
        verify(this.sessionRegistry).register(new Session(jwtUser.username(), jwtRefreshToken));
        verify(filterChain).doFilter(request, response);
        verifyNoMoreInteractions(
                request,
                response,
                filterChain
        );
    }
}
