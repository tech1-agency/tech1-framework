package io.tech1.framework.b2b.base.security.jwt.services.impl;

import io.tech1.framework.b2b.base.security.jwt.assistants.userdetails.JwtUserDetailsService;
import io.tech1.framework.b2b.base.security.jwt.tokens.facade.TokensProvider;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseRefreshTokens;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.*;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersSessionsService;
import io.tech1.framework.b2b.base.security.jwt.services.TokensContextThrowerService;
import io.tech1.framework.b2b.base.security.jwt.services.TokensService;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityJwtTokenUtils;
import io.tech1.framework.domain.exceptions.tokens.*;
import io.tech1.framework.domain.tuples.Tuple2;
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

import static io.tech1.framework.b2b.base.security.jwt.tests.random.BaseSecurityJwtRandomUtility.randomPersistedSession;
import static io.tech1.framework.b2b.base.security.jwt.tests.random.BaseSecurityJwtRandomUtility.validClaims;
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
        TokensProvider cookieProvider() {
            return mock(TokensProvider.class);
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
    // Tokens
    private final TokensProvider tokensProvider;
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
                this.tokensProvider,
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
                this.tokensProvider,
                this.securityJwtTokenUtils
        );
    }

    @Test
    void getJwtUserByAccessTokenOrThrowTest() throws AccessTokenInvalidException, RefreshTokenInvalidException, AccessTokenExpiredException, AccessTokenDbNotFoundException {
        // Arrange
        var requestAccessToken = RequestAccessToken.random();
        var requestRefreshToken = RequestRefreshToken.random();
        var accessToken = requestAccessToken.getJwtAccessToken();
        var refreshToken = requestRefreshToken.getJwtRefreshToken();
        var accessTokenValidatedClaims = JwtTokenValidatedClaims.valid(accessToken, validClaims());
        var refreshTokenValidatedClaims = JwtTokenValidatedClaims.valid(refreshToken, validClaims());
        var user = entity(JwtUser.class);
        when(this.tokensContextThrowerService.verifyValidityOrThrow(accessToken)).thenReturn(accessTokenValidatedClaims);
        when(this.tokensContextThrowerService.verifyValidityOrThrow(refreshToken)).thenReturn(refreshTokenValidatedClaims);
        when(this.jwtUserDetailsService.loadUserByUsername(accessTokenValidatedClaims.username().identifier())).thenReturn(user);

        // Act
        var actual = this.componentUnderTest.getJwtUserByAccessTokenOrThrow(requestAccessToken, requestRefreshToken);

        // Assert
        verify(this.tokensContextThrowerService).verifyValidityOrThrow(accessToken);
        verify(this.tokensContextThrowerService).verifyValidityOrThrow(refreshToken);
        verify(this.tokensContextThrowerService).verifyAccessTokenExpirationOrThrow(accessTokenValidatedClaims);
        verify(this.tokensContextThrowerService).verifyDbPresenceOrThrow(accessToken, accessTokenValidatedClaims);
        verify(this.jwtUserDetailsService).loadUserByUsername(accessTokenValidatedClaims.username().identifier());
        assertThat(actual).isEqualTo(user);
    }

    @Test
    void refreshSessionOrThrowTest() throws RefreshTokenNotFoundException, RefreshTokenInvalidException, RefreshTokenExpiredException, RefreshTokenDbNotFoundException {
        // Arrange
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var oldRequestRefreshToken = new RequestRefreshToken(randomString());
        var oldRefreshToken = oldRequestRefreshToken.getJwtRefreshToken();
        var validatedClaims = JwtTokenValidatedClaims.valid(oldRefreshToken, validClaims());
        var user = entity(JwtUser.class);
        var session = randomPersistedSession();
        var newAccessToken = JwtAccessToken.random();
        var newRefreshToken = JwtRefreshToken.random();

        when(this.tokensProvider.readRequestRefreshToken(request)).thenReturn(oldRequestRefreshToken);
        when(this.tokensContextThrowerService.verifyValidityOrThrow(oldRefreshToken)).thenReturn(validatedClaims);
        when(this.tokensContextThrowerService.verifyDbPresenceOrThrow(oldRefreshToken, validatedClaims)).thenReturn(new Tuple2<>(user, session));
        when(this.securityJwtTokenUtils.createJwtAccessToken(user.getJwtTokenCreationParams())).thenReturn(newAccessToken);
        when(this.securityJwtTokenUtils.createJwtRefreshToken(user.getJwtTokenCreationParams())).thenReturn(newRefreshToken);

        // Act
        var responseUserSession1 = this.componentUnderTest.refreshSessionOrThrow(request, response);

        // Assert
        verify(this.tokensProvider).readRequestRefreshToken(request);
        verify(this.tokensContextThrowerService).verifyValidityOrThrow(oldRefreshToken);
        verify(this.tokensContextThrowerService).verifyRefreshTokenExpirationOrThrow(validatedClaims);
        verify(this.tokensContextThrowerService).verifyDbPresenceOrThrow(oldRefreshToken, validatedClaims);
        verify(this.securityJwtTokenUtils).createJwtAccessToken(user.getJwtTokenCreationParams());
        verify(this.securityJwtTokenUtils).createJwtRefreshToken(user.getJwtTokenCreationParams());
        verify(this.baseUsersSessionsService).refresh(user, session, newAccessToken, newRefreshToken, request);
        verify(this.tokensProvider).createResponseAccessToken(newAccessToken, response);
        verify(this.tokensProvider).createResponseRefreshToken(newRefreshToken, response);
        verify(this.sessionRegistry).renew(user.username(), oldRefreshToken, newAccessToken, newRefreshToken);
        assertThat(responseUserSession1).isEqualTo(new ResponseRefreshTokens(newAccessToken, newRefreshToken));
    }
}
