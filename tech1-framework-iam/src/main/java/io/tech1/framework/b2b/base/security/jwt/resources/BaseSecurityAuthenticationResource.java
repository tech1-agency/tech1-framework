package io.tech1.framework.b2b.base.security.jwt.resources;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.tech1.framework.b2b.base.security.jwt.annotations.AbstractFrameworkBaseSecurityResource;
import io.tech1.framework.b2b.base.security.jwt.assistants.current.CurrentSessionAssistant;
import io.tech1.framework.b2b.base.security.jwt.assistants.userdetails.JwtUserDetailsService;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserLogin;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseRefreshTokens;
import io.tech1.framework.b2b.base.security.jwt.domain.security.CurrentClientUser;
import io.tech1.framework.b2b.base.security.jwt.domain.sessions.Session;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersSessionsService;
import io.tech1.framework.b2b.base.security.jwt.services.TokensService;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.b2b.base.security.jwt.tokens.facade.TokensProvider;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityJwtTokenUtils;
import io.tech1.framework.foundation.domain.exceptions.tokens.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import static io.tech1.framework.foundation.domain.enums.Status.COMPLETED;
import static io.tech1.framework.foundation.domain.enums.Status.STARTED;
import static java.util.Objects.nonNull;

// Swagger
@Tag(name = "[tech1-framework] Authentication API")
// Spring
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
    // Utilities
    private final SecurityJwtTokenUtils securityJwtTokenUtils;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public CurrentClientUser login(@RequestBody @Valid RequestUserLogin request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        var username = request.username();
        var password = request.password();
        LOGGER.info("Login attempt. Username: `{}`. Status: `{}`", username, STARTED);

        var authenticationToken = new UsernamePasswordAuthenticationToken(username.value(), password.value());
        var authentication = this.authenticationManager.authenticate(authenticationToken);

        var user = this.jwtUserDetailsService.loadUserByUsername(username.value());

        var accessToken = this.securityJwtTokenUtils.createJwtAccessToken(user.getJwtTokenCreationParams());
        var refreshToken = this.securityJwtTokenUtils.createJwtRefreshToken(user.getJwtTokenCreationParams());

        this.baseUsersSessionsService.save(user, accessToken, refreshToken, httpRequest);

        this.tokensProvider.createResponseAccessToken(accessToken, httpResponse);
        this.tokensProvider.createResponseRefreshToken(refreshToken, httpResponse);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        LOGGER.info("Login attempt. Username: `{}`. Status: `{}`", username, COMPLETED);

        this.sessionRegistry.register(new Session(username, accessToken, refreshToken));

        return this.currentSessionAssistant.getCurrentClientUser();
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws AccessTokenNotFoundException {
        var cookie = this.tokensProvider.readRequestAccessToken(httpRequest);
        if (nonNull(cookie.value())) {
            var accessToken = cookie.getJwtAccessToken();
            var validatedClaims = this.securityJwtTokenUtils.validate(accessToken);
            if (validatedClaims.valid()) {
                var username = validatedClaims.username();
                this.sessionRegistry.logout(username, accessToken);
                this.tokensProvider.clearTokens(httpResponse);
                SecurityContextHolder.clearContext();
                var session = httpRequest.getSession(false);
                if (nonNull(session)) {
                    session.invalidate();
                }
                LOGGER.info("Logout attempt completed successfully. Username: {}", username);
            }
        }
    }

    @PostMapping("/refreshToken")
    @ResponseStatus(HttpStatus.OK)
    public ResponseRefreshTokens refreshToken(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws TokenUnauthorizedException {
        try {
            return this.tokensService.refreshSessionOrThrow(httpRequest, httpResponse);
        } catch (
                RefreshTokenNotFoundException |
                RefreshTokenInvalidException |
                RefreshTokenExpiredException |
                RefreshTokenDbNotFoundException ex
        ) {
            this.tokensProvider.clearTokens(httpResponse);
            throw new TokenUnauthorizedException(ex.getMessage());
        }
    }
}
