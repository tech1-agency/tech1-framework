package tech1.framework.iam.services.base;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import tech1.framework.foundation.domain.exceptions.tokens.*;
import tech1.framework.foundation.domain.tuples.Tuple2;
import tech1.framework.iam.assistants.userdetails.JwtUserDetailsService;
import tech1.framework.iam.domain.dto.responses.ResponseRefreshTokens;
import tech1.framework.iam.domain.jwt.*;
import tech1.framework.iam.services.BaseUsersSessionsService;
import tech1.framework.iam.services.TokensContextThrowerService;
import tech1.framework.iam.services.TokensService;
import tech1.framework.iam.sessions.SessionRegistry;
import tech1.framework.iam.tokens.facade.TokensProvider;
import tech1.framework.iam.utils.SecurityJwtTokenUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static tech1.framework.foundation.utilities.random.EntityUtility.entity;
import static tech1.framework.foundation.utilities.random.RandomUtility.randomString;
import static tech1.framework.iam.domain.db.UserSession.randomPersistedSession;
import static tech1.framework.iam.tests.random.BaseSecurityJwtRandomUtility.validClaims;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BaseTokensServiceTest {

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
            return new BaseTokensService(
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
        when(this.jwtUserDetailsService.loadUserByUsername(accessTokenValidatedClaims.username().value())).thenReturn(user);

        // Act
        var actual = this.componentUnderTest.getJwtUserByAccessTokenOrThrow(requestAccessToken, requestRefreshToken);

        // Assert
        verify(this.tokensContextThrowerService).verifyValidityOrThrow(accessToken);
        verify(this.tokensContextThrowerService).verifyValidityOrThrow(refreshToken);
        verify(this.tokensContextThrowerService).verifyAccessTokenExpirationOrThrow(accessTokenValidatedClaims);
        verify(this.tokensContextThrowerService).verifyDbPresenceOrThrow(accessToken, accessTokenValidatedClaims);
        verify(this.jwtUserDetailsService).loadUserByUsername(accessTokenValidatedClaims.username().value());
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
