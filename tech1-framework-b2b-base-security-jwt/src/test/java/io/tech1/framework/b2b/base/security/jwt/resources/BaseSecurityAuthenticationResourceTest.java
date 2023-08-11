package io.tech1.framework.b2b.base.security.jwt.resources;

import io.jsonwebtoken.Claims;
import io.tech1.framework.b2b.base.security.jwt.assistants.current.CurrentSessionAssistant;
import io.tech1.framework.b2b.base.security.jwt.assistants.userdetails.JwtUserDetailsService;
import io.tech1.framework.b2b.base.security.jwt.cookies.CookieProvider;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserLogin;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseRefreshTokens;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.*;
import io.tech1.framework.b2b.base.security.jwt.domain.sessions.Session;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersSessionsService;
import io.tech1.framework.b2b.base.security.jwt.services.TokensService;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.b2b.base.security.jwt.tests.runners.AbstractResourcesRunner;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityJwtTokenUtils;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseAuthenticationRequestsValidator;
import io.tech1.framework.domain.exceptions.cookie.CookieRefreshTokenDbNotFoundException;
import io.tech1.framework.domain.exceptions.cookie.CookieRefreshTokenExpiredException;
import io.tech1.framework.domain.exceptions.cookie.CookieRefreshTokenInvalidException;
import io.tech1.framework.domain.exceptions.cookie.CookieRefreshTokenNotFoundException;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomUsername;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BaseSecurityAuthenticationResourceTest extends AbstractResourcesRunner {

    private static Stream<Arguments> refreshTokenThrowCookieUnauthorizedExceptionsTest() {
        return Stream.of(
                Arguments.of(new CookieRefreshTokenNotFoundException()),
                Arguments.of(new CookieRefreshTokenInvalidException()),
                Arguments.of( new CookieRefreshTokenExpiredException(randomUsername())),
                Arguments.of(new CookieRefreshTokenDbNotFoundException(randomUsername()))
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
    // Cookies
    private final CookieProvider cookieProvider;
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
                this.cookieProvider,
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
                this.cookieProvider,
                this.baseAuthenticationRequestsValidator,
                this.securityJwtTokenUtils
        );
    }

    @Test
    void loginTest() throws Exception {
        // Arrange
        var requestUserLogin = entity(RequestUserLogin.class);
        var username = requestUserLogin.username();
        var password = requestUserLogin.password();
        var jwtUser = entity(JwtUser.class);
        when(this.jwtUserDetailsService.loadUserByUsername(username.identifier())).thenReturn(jwtUser);
        var accessToken = entity(JwtAccessToken.class);
        var refreshToken = entity(JwtRefreshToken.class);
        when(this.securityJwtTokenUtils.createJwtAccessToken(jwtUser.getJwtTokenCreationParams())).thenReturn(accessToken);
        when(this.securityJwtTokenUtils.createJwtRefreshToken(jwtUser.getJwtTokenCreationParams())).thenReturn(refreshToken);
        var currentClientUser = randomCurrentClientUser();
        when(this.currentSessionAssistant.getCurrentClientUser()).thenReturn(currentClientUser);

        // Act
        this.mvc.perform(
                        post("/authentication/login")
                                .content(this.objectMapper.writeValueAsString(requestUserLogin))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", equalTo(currentClientUser.getUsername().identifier())))
                .andExpect(jsonPath("$.email", equalTo(currentClientUser.getEmail().value())))
                .andExpect(jsonPath("$.name", equalTo(currentClientUser.getName())))
                .andExpect(jsonPath("$.zoneId", equalTo(currentClientUser.getZoneId().getId())))
                .andExpect(jsonPath("$.authorities", notNullValue()))
                .andExpect(jsonPath("$.attributes", notNullValue()));

        // Assert
        verify(this.baseAuthenticationRequestsValidator).validateLoginRequest(requestUserLogin);
        verify(this.authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(username.identifier(), password.value()));
        verify(this.jwtUserDetailsService).loadUserByUsername(username.identifier());
        verify(this.securityJwtTokenUtils).createJwtAccessToken(jwtUser.getJwtTokenCreationParams());
        verify(this.securityJwtTokenUtils).createJwtRefreshToken(jwtUser.getJwtTokenCreationParams());
        verify(this.baseUsersSessionsService).save(eq(jwtUser), eq(accessToken), eq(refreshToken), any(HttpServletRequest.class));
        verify(this.cookieProvider).createJwtAccessCookie(eq(accessToken), any(HttpServletResponse.class));
        verify(this.cookieProvider).createJwtRefreshCookie(eq(refreshToken), any(HttpServletResponse.class));
        // WARNING: no verifications on static SecurityContextHolder
        verify(this.sessionRegistry).register(new Session(username, accessToken, refreshToken));
        verify(this.currentSessionAssistant).getCurrentClientUser();
    }

    @Test
    void logoutNoJwtRefreshTokenTest() throws Exception {
        // Arrange
        var cookieRefreshToken = new CookieAccessToken(null);
        when(this.cookieProvider.readJwtAccessToken(any(HttpServletRequest.class))).thenReturn(cookieRefreshToken);

        // Act
        this.mvc.perform(post("/authentication/logout"))
                .andExpect(status().isOk());

        // Assert
        verify(this.cookieProvider).readJwtAccessToken(any(HttpServletRequest.class));
    }

    @Test
    void logoutInvalidJwtRefreshTokenTest() throws Exception {
        // Arrange
        var cookie = new CookieAccessToken(randomString());
        var accessToken = cookie.getJwtAccessToken();
        when(this.cookieProvider.readJwtAccessToken(any(HttpServletRequest.class))).thenReturn(cookie);
        when(this.securityJwtTokenUtils.validate(accessToken)).thenReturn(JwtTokenValidatedClaims.invalid(accessToken));

        // Act
        this.mvc.perform(post("/authentication/logout"))
                .andExpect(status().isOk());

        // Assert
        verify(this.cookieProvider).readJwtAccessToken(any(HttpServletRequest.class));
        verify(this.securityJwtTokenUtils).validate(accessToken);
    }

    @Test
    void logoutTest() throws Exception {
        // Arrange
        var httpSession = mock(HttpSession.class);
        var username = randomUsername();
        var cookie = entity(CookieAccessToken.class);
        var accessToken = cookie.getJwtAccessToken();
        var claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(username.identifier());
        when(this.cookieProvider.readJwtAccessToken(any(HttpServletRequest.class))).thenReturn(cookie);
        var validatedClaims = JwtTokenValidatedClaims.valid(accessToken, claims);
        when(this.securityJwtTokenUtils.validate(cookie.getJwtAccessToken())).thenReturn(validatedClaims);

        // Act
        this.mvc.perform(post("/authentication/logout")
                        .with(request -> {
                            request.setSession(httpSession);
                            return request;
                        })
                )
                .andExpect(status().isOk());

        // Assert
        verify(this.cookieProvider).readJwtAccessToken(any(HttpServletRequest.class));
        verify(this.securityJwtTokenUtils).validate(accessToken);
        verify(this.sessionRegistry).logout(username, accessToken);
        verify(this.cookieProvider).clearCookies(any(HttpServletResponse.class));
        verify(httpSession).invalidate();
        // WARNING: no verifications on static SecurityContextHolder
    }

    @Test
    void logoutNullSessionTest() throws Exception {
        // Arrange
        var username = randomUsername();
        var cookie = entity(CookieAccessToken.class);
        var accessToken = cookie.getJwtAccessToken();
        var claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(username.identifier());
        when(this.cookieProvider.readJwtAccessToken(any(HttpServletRequest.class))).thenReturn(cookie);
        var validatedClaims = JwtTokenValidatedClaims.valid(accessToken, claims);
        when(this.securityJwtTokenUtils.validate(accessToken)).thenReturn(validatedClaims);

        // Act
        this.mvc.perform(post("/authentication/logout"))
                .andExpect(status().isOk());

        // Assert
        verify(this.cookieProvider).readJwtAccessToken(any(HttpServletRequest.class));
        verify(this.securityJwtTokenUtils).validate(accessToken);
        verify(this.sessionRegistry).logout(username, accessToken);
        verify(this.cookieProvider).clearCookies(any(HttpServletResponse.class));
        // WARNING: no verifications on static SecurityContextHolder
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
        verify(this.cookieProvider).clearCookies(any());
        reset(
                this.tokensService,
                this.cookieProvider
        );
    }

    @Test
    void refreshTokenValidTest() throws Exception {
        // Arrange
        var userSession1 = entity(ResponseRefreshTokens.class);
        when(this.tokensService.refreshSessionOrThrow(any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(userSession1);

        // Act
        this.mvc.perform(post("/authentication/refreshToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", equalTo(userSession1.accessToken().value())))
                .andExpect(jsonPath("$.refreshToken", equalTo(userSession1.refreshToken().value())));

        // Assert
        verify(this.tokensService).refreshSessionOrThrow(any(HttpServletRequest.class), any(HttpServletResponse.class));
    }
}
