package io.tech1.framework.b2b.base.security.jwt.resources;

import io.tech1.framework.b2b.base.security.jwt.annotations.AbstractFrameworkBaseSecurityResource;
import io.tech1.framework.b2b.base.security.jwt.assistants.current.CurrentSessionAssistant;
import io.tech1.framework.b2b.base.security.jwt.assistants.userdetails.JwtUserDetailsService;
import io.tech1.framework.b2b.base.security.jwt.tokens.facade.TokensProvider;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserLogin;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseRefreshTokens;
import io.tech1.framework.b2b.base.security.jwt.domain.security.CurrentClientUser;
import io.tech1.framework.b2b.base.security.jwt.domain.sessions.Session;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersSessionsService;
import io.tech1.framework.b2b.base.security.jwt.services.TokensService;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityJwtTokenUtils;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseAuthenticationRequestsValidator;
import io.tech1.framework.domain.exceptions.tokens.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static io.tech1.framework.domain.enums.Status.COMPLETED;
import static io.tech1.framework.domain.enums.Status.STARTED;
import static java.util.Objects.nonNull;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@AbstractFrameworkBaseSecurityResource
@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseSecurityAuthenticationResource {

    // Authentication
    private final AuthenticationManager authenticationManager;
    // Assistants
    private final CurrentSessionAssistant currentSessionAssistant;
    private final JwtUserDetailsService jwtUserDetailsService;
    // Sessions
    private final SessionRegistry sessionRegistry;
    // Services
    private final BaseUsersSessionsService baseUsersSessionsService;
    private final TokensService tokensService;
    // Tokens
    private final TokensProvider tokensProvider;
    // Validators
    private final BaseAuthenticationRequestsValidator baseAuthenticationRequestsValidator;
    // Utilities
    private final SecurityJwtTokenUtils securityJwtTokenUtils;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public CurrentClientUser login(@RequestBody RequestUserLogin requestUserLogin, HttpServletRequest request, HttpServletResponse response) {
        this.baseAuthenticationRequestsValidator.validateLoginRequest(requestUserLogin);
        var username = requestUserLogin.username();
        var password = requestUserLogin.password();
        LOGGER.info("Login attempt. Username: `{}`. Status: `{}`", username, STARTED);

        var authenticationToken = new UsernamePasswordAuthenticationToken(username.identifier(), password.value());
        var authentication = this.authenticationManager.authenticate(authenticationToken);

        var user = this.jwtUserDetailsService.loadUserByUsername(username.identifier());

        var accessToken = this.securityJwtTokenUtils.createJwtAccessToken(user.getJwtTokenCreationParams());
        var refreshToken = this.securityJwtTokenUtils.createJwtRefreshToken(user.getJwtTokenCreationParams());

        this.baseUsersSessionsService.save(user, accessToken, refreshToken, request);

        this.tokensProvider.createResponseAccessToken(accessToken, response);
        this.tokensProvider.createResponseRefreshToken(refreshToken, response);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        LOGGER.info("Login attempt. Username: `{}`. Status: `{}`", username, COMPLETED);

        this.sessionRegistry.register(new Session(username, accessToken, refreshToken));

        return this.currentSessionAssistant.getCurrentClientUser();
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpServletRequest request, HttpServletResponse response) throws AccessTokenNotFoundException {
        var cookie = this.tokensProvider.readRequestAccessToken(request);
        if (nonNull(cookie.value())) {
            var accessToken = cookie.getJwtAccessToken();
            var validatedClaims = this.securityJwtTokenUtils.validate(accessToken);
            if (validatedClaims.valid()) {
                var username = validatedClaims.username();
                this.sessionRegistry.logout(username, accessToken);
                this.tokensProvider.clearTokens(response);
                SecurityContextHolder.clearContext();
                var session = request.getSession(false);
                if (nonNull(session)) {
                    session.invalidate();
                }
                LOGGER.debug("Logout attempt completed successfully. Username: {}", username);
            }
        }
    }

    @PostMapping("/refreshToken")
    @ResponseStatus(HttpStatus.OK)
    public ResponseRefreshTokens refreshToken(HttpServletRequest request, HttpServletResponse response) throws TokenUnauthorizedException {
        try {
            return this.tokensService.refreshSessionOrThrow(request, response);
        } catch (RefreshTokenNotFoundException | RefreshTokenInvalidException |
                 RefreshTokenExpiredException | RefreshTokenDbNotFoundException ex) {
            this.tokensProvider.clearTokens(response);
            throw new TokenUnauthorizedException(ex.getMessage());
        }
    }
}
