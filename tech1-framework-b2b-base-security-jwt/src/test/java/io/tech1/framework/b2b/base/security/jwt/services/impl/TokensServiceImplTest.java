package io.tech1.framework.b2b.base.security.jwt.services.impl;

import io.tech1.framework.b2b.base.security.jwt.assistants.userdetails.JwtUserDetailsService;
import io.tech1.framework.b2b.base.security.jwt.cookies.CookieProvider;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseRefreshTokens;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.*;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersSessionsService;
import io.tech1.framework.b2b.base.security.jwt.services.TokensContextThrowerService;
import io.tech1.framework.b2b.base.security.jwt.services.TokensService;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityJwtTokenUtils;
import io.tech1.framework.domain.exceptions.cookie.*;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static io.tech1.framework.b2b.base.security.jwt.tests.random.BaseSecurityJwtRandomUtility.validDefaultClaims;
import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class TokensServiceImplTest {

    @Configuration
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        @Bean
        JwtUserDetailsService jwtUserDetailsAssistant() {
            return mock(JwtUserDetailsService.class);
        }

        @Bean
        SessionRegistry sessionRegistry() {
            return mock(SessionRegistry.class);
        }

        @Bean
        TokensContextThrowerService tokenContextThrowerService() {
            return mock(TokensContextThrowerService.class);
        }

        @Bean
        BaseUsersSessionsService baseUsersSessionsService() {
            return mock(BaseUsersSessionsService.class);
        }

        @Bean
        CookieProvider cookieProvider() {
            return mock(CookieProvider.class);
        }

        @Bean
        SecurityJwtTokenUtils securityJwtTokenUtility() {
            return mock(SecurityJwtTokenUtils.class);
        }

        @Bean
        TokensService tokenService() {
            return new TokensServiceImpl(
                    this.jwtUserDetailsAssistant(),
                    this.sessionRegistry(),
                    this.tokenContextThrowerService(),
                    this.baseUsersSessionsService(),
                    this.cookieProvider(),
                    this.securityJwtTokenUtility()
            );
        }
    }

    // Assistants
    private final JwtUserDetailsService jwtUserDetailsService;
    // Session
    private final SessionRegistry sessionRegistry;
    // Services
    private final TokensContextThrowerService tokensContextThrowerService;
    private final BaseUsersSessionsService baseUsersSessionsService;
    // Cookie
    private final CookieProvider cookieProvider;
    // Utilities
    private final SecurityJwtTokenUtils securityJwtTokenUtils;

    private final TokensService componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.jwtUserDetailsService,
                this.sessionRegistry,
                this.tokensContextThrowerService,
                this.baseUsersSessionsService,
                this.cookieProvider,
                this.securityJwtTokenUtils
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.jwtUserDetailsService,
                this.sessionRegistry,
                this.tokensContextThrowerService,
                this.baseUsersSessionsService,
                this.cookieProvider,
                this.securityJwtTokenUtils
        );
    }

    @Test
    void getJwtUserByAccessTokenOrThrowTest() throws CookieAccessTokenInvalidException, CookieRefreshTokenInvalidException, CookieAccessTokenExpiredException, CookieAccessTokenDbNotFoundException {
        // Arrange
        var cookieAccessToken = entity(CookieAccessToken.class);
        var cookieRefreshToken = entity(CookieRefreshToken.class);
        var accessToken = cookieAccessToken.getJwtAccessToken();
        var refreshToken = cookieRefreshToken.getJwtRefreshToken();
        var accessTokenValidatedClaims = JwtTokenValidatedClaims.valid(accessToken, validDefaultClaims());
        var refreshTokenValidatedClaims = JwtTokenValidatedClaims.valid(refreshToken, validDefaultClaims());
        var user = entity(JwtUser.class);
        when(this.tokensContextThrowerService.verifyValidityOrThrow(accessToken)).thenReturn(accessTokenValidatedClaims);
        when(this.tokensContextThrowerService.verifyValidityOrThrow(refreshToken)).thenReturn(refreshTokenValidatedClaims);
        when(this.jwtUserDetailsService.loadUserByUsername(accessTokenValidatedClaims.username().identifier())).thenReturn(user);

        // Act
        var actual = this.componentUnderTest.getJwtUserByAccessTokenOrThrow(cookieAccessToken, cookieRefreshToken);

        // Assert
        verify(this.tokensContextThrowerService).verifyValidityOrThrow(accessToken);
        verify(this.tokensContextThrowerService).verifyValidityOrThrow(refreshToken);
        verify(this.tokensContextThrowerService).verifyAccessTokenExpirationOrThrow(accessTokenValidatedClaims);
        verify(this.tokensContextThrowerService).verifyDbPresenceOrThrow(accessToken, accessTokenValidatedClaims);
        verify(this.jwtUserDetailsService).loadUserByUsername(accessTokenValidatedClaims.username().identifier());
        assertThat(actual).isEqualTo(user);
    }

    @Test
    void refreshSessionOrThrowTest() throws CookieRefreshTokenNotFoundException, CookieRefreshTokenInvalidException, CookieRefreshTokenExpiredException, CookieRefreshTokenDbNotFoundException {
        // Arrange
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var oldCookieRefreshToken = new CookieRefreshToken(randomString());
        var oldRefreshToken = oldCookieRefreshToken.getJwtRefreshToken();
        var validatedClaims = JwtTokenValidatedClaims.valid(oldRefreshToken, validDefaultClaims());
        var user = entity(JwtUser.class);
        var accessToken = entity(JwtAccessToken.class);
        var newRefreshToken = entity(JwtRefreshToken.class);
        when(this.cookieProvider.readJwtRefreshToken(request)).thenReturn(oldCookieRefreshToken);
        when(this.tokensContextThrowerService.verifyValidityOrThrow(oldRefreshToken)).thenReturn(validatedClaims);
        when(this.tokensContextThrowerService.verifyDbPresenceOrThrow(oldRefreshToken, validatedClaims)).thenReturn(user);
        when(this.securityJwtTokenUtils.createJwtAccessToken(user.getJwtTokenCreationParams())).thenReturn(accessToken);
        when(this.securityJwtTokenUtils.createJwtRefreshToken(user.getJwtTokenCreationParams())).thenReturn(newRefreshToken);

        // Act
        var responseUserSession1 = this.componentUnderTest.refreshSessionOrThrow(request, response);

        // Assert
        verify(this.cookieProvider).readJwtRefreshToken(request);
        verify(this.tokensContextThrowerService).verifyValidityOrThrow(oldRefreshToken);
        verify(this.tokensContextThrowerService).verifyRefreshTokenExpirationOrThrow(validatedClaims);
        verify(this.tokensContextThrowerService).verifyDbPresenceOrThrow(oldRefreshToken, validatedClaims);
        verify(this.securityJwtTokenUtils).createJwtAccessToken(user.getJwtTokenCreationParams());
        verify(this.securityJwtTokenUtils).createJwtRefreshToken(user.getJwtTokenCreationParams());
        verify(this.baseUsersSessionsService).refresh(user, accessToken, oldRefreshToken, newRefreshToken, request);
        verify(this.cookieProvider).createJwtAccessCookie(accessToken, response);
        verify(this.cookieProvider).createJwtRefreshCookie(newRefreshToken, response);
        verify(this.sessionRegistry).renew(user.username(), oldRefreshToken, accessToken, newRefreshToken);
        assertThat(responseUserSession1).isEqualTo(new ResponseRefreshTokens(accessToken, newRefreshToken));
    }
}
