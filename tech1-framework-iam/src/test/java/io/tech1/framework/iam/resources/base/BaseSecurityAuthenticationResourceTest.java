package io.tech1.framework.iam.resources.base;

import io.jsonwebtoken.Claims;
import io.tech1.framework.iam.tests.runners.AbstractResourcesRunner1;
import io.tech1.framework.iam.assistants.current.CurrentSessionAssistant;
import io.tech1.framework.iam.assistants.userdetails.JwtUserDetailsService;
import io.tech1.framework.iam.domain.dto.requests.RequestUserLogin;
import io.tech1.framework.iam.domain.dto.responses.ResponseRefreshTokens;
import io.tech1.framework.iam.domain.jwt.*;
import io.tech1.framework.iam.domain.security.CurrentClientUser;
import io.tech1.framework.iam.domain.sessions.Session;
import io.tech1.framework.iam.services.BaseUsersSessionsService;
import io.tech1.framework.iam.services.TokensService;
import io.tech1.framework.iam.sessions.SessionRegistry;
import io.tech1.framework.iam.tokens.facade.TokensProvider;
import io.tech1.framework.iam.utils.SecurityJwtTokenUtils;
import io.tech1.framework.iam.validators.BaseAuthenticationRequestsValidator;
import io.tech1.framework.foundation.domain.base.Username;
import io.tech1.framework.foundation.domain.exceptions.tokens.RefreshTokenDbNotFoundException;
import io.tech1.framework.foundation.domain.exceptions.tokens.RefreshTokenExpiredException;
import io.tech1.framework.foundation.domain.exceptions.tokens.RefreshTokenInvalidException;
import io.tech1.framework.foundation.domain.exceptions.tokens.RefreshTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BaseSecurityAuthenticationResourceTest extends AbstractResourcesRunner1 {

    private static Stream<Arguments> refreshTokenThrowCookieUnauthorizedExceptionsTest() {
        return Stream.of(
                Arguments.of(new RefreshTokenNotFoundException()),
                Arguments.of(new RefreshTokenInvalidException()),
                Arguments.of( new RefreshTokenExpiredException(Username.random())),
                Arguments.of(new RefreshTokenDbNotFoundException(Username.random()))
        );
    }

    // Authentication
    private final AuthenticationManager authenticationManager;
    // Session
    private final SessionRegistry sessionRegistry;
    // Services
    private final BaseUsersSessionsService baseUsersSessionsService;
    private final TokensService tokensService;
    // Assistants
    private final CurrentSessionAssistant currentSessionAssistant;
    private final JwtUserDetailsService jwtUserDetailsService;
    // Tokens
    private final TokensProvider tokensProvider;
    // Validators
    private final BaseAuthenticationRequestsValidator baseAuthenticationRequestsValidator;
    // Utilities
    private final SecurityJwtTokenUtils securityJwtTokenUtils;

    // Resource
    private final BaseSecurityAuthenticationResource componentUnderTest;

    @BeforeEach
    void beforeEach() {
        this.standaloneSetupByResourceUnderTest(this.componentUnderTest);
        reset(
                this.authenticationManager,
                this.sessionRegistry,
                this.baseUsersSessionsService,
                this.tokensService,
                this.currentSessionAssistant,
                this.jwtUserDetailsService,
                this.tokensProvider,
                this.baseAuthenticationRequestsValidator,
                this.securityJwtTokenUtils
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.authenticationManager,
                this.sessionRegistry,
                this.baseUsersSessionsService,
                this.tokensService,
                this.currentSessionAssistant,
                this.jwtUserDetailsService,
                this.tokensProvider,
                this.baseAuthenticationRequestsValidator,
                this.securityJwtTokenUtils
        );
    }

    @Test
    void loginTest() throws Exception {
        // Arrange
        var request = RequestUserLogin.testsHardcoded();
        var username = request.username();
        var password = request.password();
        var user = JwtUser.testsHardcoded();
        when(this.jwtUserDetailsService.loadUserByUsername(username.value())).thenReturn(user);
        var accessToken = JwtAccessToken.random();
        var refreshToken = JwtRefreshToken.random();
        when(this.securityJwtTokenUtils.createJwtAccessToken(user.getJwtTokenCreationParams())).thenReturn(accessToken);
        when(this.securityJwtTokenUtils.createJwtRefreshToken(user.getJwtTokenCreationParams())).thenReturn(refreshToken);
        var currentClientUser = CurrentClientUser.random();
        when(this.currentSessionAssistant.getCurrentClientUser()).thenReturn(currentClientUser);

        // Act
        this.mvc.perform(
                        post("/authentication/login")
                                .content(this.objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", equalTo(currentClientUser.getUsername().value())))
                .andExpect(jsonPath("$.email", equalTo(currentClientUser.getEmail().value())))
                .andExpect(jsonPath("$.name", equalTo(currentClientUser.getName())))
                .andExpect(jsonPath("$.zoneId", equalTo(currentClientUser.getZoneId().getId())))
                .andExpect(jsonPath("$.authorities", notNullValue()))
                .andExpect(jsonPath("$.attributes", notNullValue()));

        // Assert
        verify(this.authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(username.value(), password.value()));
        verify(this.jwtUserDetailsService).loadUserByUsername(username.value());
        verify(this.securityJwtTokenUtils).createJwtAccessToken(user.getJwtTokenCreationParams());
        verify(this.securityJwtTokenUtils).createJwtRefreshToken(user.getJwtTokenCreationParams());
        verify(this.baseUsersSessionsService).save(eq(user), eq(accessToken), eq(refreshToken), any(HttpServletRequest.class));
        verify(this.tokensProvider).createResponseAccessToken(eq(accessToken), any(HttpServletResponse.class));
        verify(this.tokensProvider).createResponseRefreshToken(eq(refreshToken), any(HttpServletResponse.class));
        // no verifications on static SecurityContextHolder
        verify(this.sessionRegistry).register(new Session(username, accessToken, refreshToken));
        verify(this.currentSessionAssistant).getCurrentClientUser();
    }

    @Test
    void logoutNoJwtRefreshTokenTest() throws Exception {
        // Arrange
        var requestAccessToken = new RequestAccessToken(null);
        when(this.tokensProvider.readRequestAccessToken(any(HttpServletRequest.class))).thenReturn(requestAccessToken);

        // Act
        this.mvc.perform(post("/authentication/logout"))
                .andExpect(status().isOk());

        // Assert
        verify(this.tokensProvider).readRequestAccessToken(any(HttpServletRequest.class));
    }

    @Test
    void logoutInvalidJwtRefreshTokenTest() throws Exception {
        // Arrange
        var requestAccessToken = RequestAccessToken.random();
        var accessToken = requestAccessToken.getJwtAccessToken();
        when(this.tokensProvider.readRequestAccessToken(any(HttpServletRequest.class))).thenReturn(requestAccessToken);
        when(this.securityJwtTokenUtils.validate(accessToken)).thenReturn(JwtTokenValidatedClaims.invalid(accessToken));

        // Act
        this.mvc.perform(post("/authentication/logout"))
                .andExpect(status().isOk());

        // Assert
        verify(this.tokensProvider).readRequestAccessToken(any(HttpServletRequest.class));
        verify(this.securityJwtTokenUtils).validate(accessToken);
    }

    @Test
    void logoutTest() throws Exception {
        // Arrange
        var httpSession = mock(HttpSession.class);
        var username = Username.random();
        var requestAccessToken = RequestAccessToken.random();
        var accessToken = requestAccessToken.getJwtAccessToken();
        var claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(username.value());
        when(this.tokensProvider.readRequestAccessToken(any(HttpServletRequest.class))).thenReturn(requestAccessToken);
        var validatedClaims = JwtTokenValidatedClaims.valid(accessToken, claims);
        when(this.securityJwtTokenUtils.validate(requestAccessToken.getJwtAccessToken())).thenReturn(validatedClaims);

        // Act
        this.mvc.perform(post("/authentication/logout")
                        .with(request -> {
                            request.setSession(httpSession);
                            return request;
                        })
                )
                .andExpect(status().isOk());

        // Assert
        verify(this.tokensProvider).readRequestAccessToken(any(HttpServletRequest.class));
        verify(this.securityJwtTokenUtils).validate(accessToken);
        verify(this.sessionRegistry).logout(username, accessToken);
        verify(this.tokensProvider).clearTokens(any(HttpServletResponse.class));
        verify(httpSession).invalidate();
        // no verifications on static SecurityContextHolder
    }

    @Test
    void logoutNullSessionTest() throws Exception {
        // Arrange
        var username = Username.random();
        var requestAccessToken = RequestAccessToken.random();
        var accessToken = requestAccessToken.getJwtAccessToken();
        var claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(username.value());
        when(this.tokensProvider.readRequestAccessToken(any(HttpServletRequest.class))).thenReturn(requestAccessToken);
        var validatedClaims = JwtTokenValidatedClaims.valid(accessToken, claims);
        when(this.securityJwtTokenUtils.validate(accessToken)).thenReturn(validatedClaims);

        // Act
        this.mvc.perform(post("/authentication/logout"))
                .andExpect(status().isOk());

        // Assert
        verify(this.tokensProvider).readRequestAccessToken(any(HttpServletRequest.class));
        verify(this.securityJwtTokenUtils).validate(accessToken);
        verify(this.sessionRegistry).logout(username, accessToken);
        verify(this.tokensProvider).clearTokens(any(HttpServletResponse.class));
        // no verifications on static SecurityContextHolder
    }

    @ParameterizedTest
    @MethodSource("refreshTokenThrowCookieUnauthorizedExceptionsTest")
    void refreshTokenThrowCookieUnauthorizedExceptionsTest(Exception exception) throws Exception {
        // Arrange
        when(this.tokensService.refreshSessionOrThrow(any(HttpServletRequest.class), any(HttpServletResponse.class))).thenThrow(exception);

        // Act
        this.mvc.perform(post("/authentication/refreshToken"))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()))
                .andExpect(jsonPath("$.exceptionEntityType", equalTo("ERROR")))
                .andExpect(jsonPath("$.attributes.shortMessage", equalTo(exception.getMessage())))
                .andExpect(jsonPath("$.attributes.fullMessage", equalTo(exception.getMessage())));

        // Assert
        verify(this.tokensService).refreshSessionOrThrow(any(HttpServletRequest.class), any(HttpServletResponse.class));
        verify(this.tokensProvider).clearTokens(any());
        reset(
                this.tokensService,
                this.tokensProvider
        );
    }

    @Test
    void refreshTokenValidTest() throws Exception {
        // Arrange
        var response = ResponseRefreshTokens.random();
        when(this.tokensService.refreshSessionOrThrow(any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(response);

        // Act
        this.mvc.perform(post("/authentication/refreshToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", equalTo(response.accessToken().value())))
                .andExpect(jsonPath("$.refreshToken", equalTo(response.refreshToken().value())));

        // Assert
        verify(this.tokensService).refreshSessionOrThrow(any(HttpServletRequest.class), any(HttpServletResponse.class));
    }
}
