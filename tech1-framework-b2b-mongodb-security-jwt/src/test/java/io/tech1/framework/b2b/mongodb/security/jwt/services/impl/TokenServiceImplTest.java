package io.tech1.framework.b2b.mongodb.security.jwt.services.impl;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.mongodb.security.jwt.assistants.userdetails.JwtUserDetailsAssistant;
import io.tech1.framework.b2b.mongodb.security.jwt.cookies.CookieProvider;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUserSession;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.*;
import io.tech1.framework.b2b.base.security.jwt.domain.sessions.Session;
import io.tech1.framework.b2b.mongodb.security.jwt.services.TokenContextThrowerService;
import io.tech1.framework.b2b.mongodb.security.jwt.services.TokenService;
import io.tech1.framework.b2b.mongodb.security.jwt.services.UserSessionService;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.b2b.mongodb.security.jwt.utilities.SecurityJwtTokenUtility;
import io.tech1.framework.domain.exceptions.cookie.*;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static io.tech1.framework.b2b.mongodb.security.jwt.tests.random.SecurityJwtRandomUtility.randomValidDefaultClaims;
import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class TokenServiceImplTest {

    @Configuration
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        @Bean
        JwtUserDetailsAssistant jwtUserDetailsAssistant() {
            return mock(JwtUserDetailsAssistant.class);
        }

        @Bean
        SessionRegistry sessionRegistry() {
            return mock(SessionRegistry.class);
        }

        @Bean
        TokenContextThrowerService tokenContextThrowerService() {
            return mock(TokenContextThrowerService.class);
        }

        @Bean
        UserSessionService userSessionService() {
            return mock(UserSessionService.class);
        }

        @Bean
        CookieProvider cookieProvider() {
            return mock(CookieProvider.class);
        }

        @Bean
        SecurityJwtTokenUtility securityJwtTokenUtility() {
            return mock(SecurityJwtTokenUtility.class);
        }

        @Bean
        TokenService tokenService() {
            return new TokenServiceImpl(
                    this.jwtUserDetailsAssistant(),
                    this.sessionRegistry(),
                    this.tokenContextThrowerService(),
                    this.userSessionService(),
                    this.cookieProvider(),
                    this.securityJwtTokenUtility()
            );
        }
    }

    // Assistants
    private final JwtUserDetailsAssistant jwtUserDetailsAssistant;
    // Session
    private final SessionRegistry sessionRegistry;
    // Services
    private final TokenContextThrowerService tokenContextThrowerService;
    private final UserSessionService userSessionService;
    // Cookie
    private final CookieProvider cookieProvider;
    // Utilities
    private final SecurityJwtTokenUtility securityJwtTokenUtility;

    private final TokenService componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.jwtUserDetailsAssistant,
                this.sessionRegistry,
                this.tokenContextThrowerService,
                this.userSessionService,
                this.cookieProvider,
                this.securityJwtTokenUtility
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.jwtUserDetailsAssistant,
                this.sessionRegistry,
                this.tokenContextThrowerService,
                this.userSessionService,
                this.cookieProvider,
                this.securityJwtTokenUtility
        );
    }

    @Test
    void getJwtUserByAccessTokenOrThrowTest() throws CookieAccessTokenInvalidException, CookieRefreshTokenInvalidException, CookieAccessTokenExpiredException {
        // Arrange
        var cookieAccessToken = entity(CookieAccessToken.class);
        var cookieRefreshToken = entity(CookieRefreshToken.class);
        var jwtAccessToken = cookieAccessToken.getJwtAccessToken();
        var jwtRefreshToken = cookieRefreshToken.getJwtRefreshToken();
        var accessTokenValidatedClaims = JwtTokenValidatedClaims.valid(jwtAccessToken, randomValidDefaultClaims());
        var refreshTokenValidatedClaims = JwtTokenValidatedClaims.valid(jwtRefreshToken, randomValidDefaultClaims());
        var jwtUser = entity(JwtUser.class);
        when(this.tokenContextThrowerService.verifyValidityOrThrow(jwtAccessToken)).thenReturn(accessTokenValidatedClaims);
        when(this.tokenContextThrowerService.verifyValidityOrThrow(jwtRefreshToken)).thenReturn(refreshTokenValidatedClaims);
        when(this.jwtUserDetailsAssistant.loadUserByUsername(accessTokenValidatedClaims.safeGetUsername().identifier())).thenReturn(jwtUser);

        // Act
        var tuple2 = this.componentUnderTest.getJwtUserByAccessTokenOrThrow(cookieAccessToken, cookieRefreshToken);

        // Assert
        verify(this.tokenContextThrowerService).verifyValidityOrThrow(jwtAccessToken);
        verify(this.tokenContextThrowerService).verifyValidityOrThrow(jwtRefreshToken);
        verify(this.tokenContextThrowerService).verifyAccessTokenExpirationOrThrow(accessTokenValidatedClaims);
        verify(this.jwtUserDetailsAssistant).loadUserByUsername(accessTokenValidatedClaims.safeGetUsername().identifier());
        assertThat(tuple2).isNotNull();
        assertThat(tuple2.a()).isEqualTo(jwtUser);
        assertThat(tuple2.b()).isEqualTo(jwtRefreshToken);
    }

    @Test
    void refreshSessionOrThrowTest() throws CookieRefreshTokenNotFoundException, CookieRefreshTokenInvalidException, CookieRefreshTokenExpiredException, CookieRefreshTokenDbNotFoundException {
        // Arrange
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var oldCookieRefreshToken = new CookieRefreshToken(randomString());
        var oldJwtRefreshToken = oldCookieRefreshToken.getJwtRefreshToken();
        var validatedClaims = JwtTokenValidatedClaims.valid(oldJwtRefreshToken, randomValidDefaultClaims());
        var user = entity(DbUser.class);
        var jwtAccessToken = entity(JwtAccessToken.class);
        var newJwtRefreshToken = entity(JwtRefreshToken.class);
        var userSession = entity(DbUserSession.class);
        when(this.cookieProvider.readJwtRefreshToken(request)).thenReturn(oldCookieRefreshToken);
        when(this.tokenContextThrowerService.verifyValidityOrThrow(oldJwtRefreshToken)).thenReturn(validatedClaims);
        when(this.tokenContextThrowerService.verifyDbPresenceOrThrow(validatedClaims, oldJwtRefreshToken)).thenReturn(user);
        when(this.securityJwtTokenUtility.createJwtAccessToken(user)).thenReturn(jwtAccessToken);
        when(this.securityJwtTokenUtility.createJwtRefreshToken(user)).thenReturn(newJwtRefreshToken);
        when(this.userSessionService.refresh(user, oldCookieRefreshToken.getJwtRefreshToken(), newJwtRefreshToken, request)).thenReturn(userSession);

        // Act
        var responseUserSession1 = this.componentUnderTest.refreshSessionOrThrow(request, response);

        // Assert
        verify(this.cookieProvider).readJwtRefreshToken(request);
        verify(this.tokenContextThrowerService).verifyValidityOrThrow(oldJwtRefreshToken);
        verify(this.tokenContextThrowerService).verifyRefreshTokenExpirationOrThrow(validatedClaims);
        verify(this.tokenContextThrowerService).verifyDbPresenceOrThrow(validatedClaims, oldJwtRefreshToken);
        verify(this.securityJwtTokenUtility).createJwtAccessToken(user);
        verify(this.securityJwtTokenUtility).createJwtRefreshToken(user);
        verify(this.userSessionService).refresh(user, oldJwtRefreshToken, newJwtRefreshToken, request);
        verify(this.cookieProvider).createJwtAccessCookie(jwtAccessToken, response);
        verify(this.cookieProvider).createJwtRefreshCookie(newJwtRefreshToken, response);
        var oldSessionAC = ArgumentCaptor.forClass(Session.class);
        var newSessionAC = ArgumentCaptor.forClass(Session.class);
        verify(this.sessionRegistry).renew(oldSessionAC.capture(), newSessionAC.capture());
        var oldSession = oldSessionAC.getValue();
        assertThat(oldSession).isNotNull();
        assertThat(oldSession.username()).isEqualTo(user.getUsername());
        assertThat(oldSession.refreshToken().value()).isEqualTo(oldJwtRefreshToken.value());
        var newSession = newSessionAC.getValue();
        assertThat(newSession).isNotNull();
        assertThat(newSession.username()).isEqualTo(user.getUsername());
        assertThat(newSession.refreshToken().value()).isEqualTo(newJwtRefreshToken.value());
        assertThat(responseUserSession1).isNotNull();
        assertThat(responseUserSession1.refreshToken()).isEqualTo(userSession.getJwtRefreshToken().value());
    }
}
