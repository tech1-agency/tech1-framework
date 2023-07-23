package io.tech1.framework.b2b.mongodb.security.jwt.filters;

import io.tech1.framework.b2b.mongodb.security.jwt.cookies.CookieProvider;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.session.Session;
import io.tech1.framework.b2b.mongodb.security.jwt.services.TokenService;
import io.tech1.framework.b2b.mongodb.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.domain.exceptions.cookie.*;
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
                Arguments.of(new CookieAccessTokenNotFoundException()),
                Arguments.of(new CookieAccessTokenExpiredException(randomUsername()))
        );
    }

    private static Stream<Arguments> clearCookieTest() {
        return Stream.of(
                Arguments.of(new CookieAccessTokenInvalidException()),
                Arguments.of(new CookieRefreshTokenInvalidException()),
                Arguments.of(new CookieRefreshTokenNotFoundException())
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
        when(this.tokenService.getJwtUserByAccessTokenOrThrow(eq(request))).thenThrow(exception);

        // Act
        this.componentUnderTest.doFilterInternal(request, response, filterChain);

        // Assert
        verify(this.tokenService).getJwtUserByAccessTokenOrThrow(eq(request));
        verify(filterChain).doFilter(eq(request), eq(response));
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
        when(this.tokenService.getJwtUserByAccessTokenOrThrow(eq(request))).thenThrow(exception);

        // Act
        this.componentUnderTest.doFilterInternal(request, response, filterChain);

        // Assert
        verify(this.tokenService).getJwtUserByAccessTokenOrThrow(eq(request));
        verify(this.cookieProvider).clearCookies(response);
        verify(response).sendError(eq(HttpStatus.UNAUTHORIZED.value()));
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
        when(this.tokenService.getJwtUserByAccessTokenOrThrow(eq(request))).thenReturn(new Tuple2<>(jwtUser, jwtRefreshToken));

        // Act
        this.componentUnderTest.doFilterInternal(request, response, filterChain);

        // Assert
        verify(this.tokenService).getJwtUserByAccessTokenOrThrow(eq(request));
        // WARNING: no verifications on static SecurityContextHolder
        verify(this.sessionRegistry).register(eq(new Session(jwtUser.getDbUser().getUsername(), jwtRefreshToken)));
        verify(filterChain).doFilter(eq(request), eq(response));
        verifyNoMoreInteractions(
                request,
                response,
                filterChain
        );
    }
}
