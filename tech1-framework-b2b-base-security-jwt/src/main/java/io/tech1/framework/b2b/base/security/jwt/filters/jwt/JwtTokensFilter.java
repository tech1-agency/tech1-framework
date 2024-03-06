package io.tech1.framework.b2b.base.security.jwt.filters.jwt;

import io.tech1.framework.b2b.base.security.jwt.domain.sessions.Session;
import io.tech1.framework.b2b.base.security.jwt.services.TokensService;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.b2b.base.security.jwt.tokens.facade.TokensProvider;
import io.tech1.framework.domain.exceptions.tokens.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtTokensFilter extends OncePerRequestFilter {

    // Session
    private final SessionRegistry sessionRegistry;
    // Services
    private final TokensService tokensService;
    // Tokens
    private final TokensProvider tokensProvider;
    // Filters
    private final JwtTokensFilterExtension jwtTokensFilterExtension;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {
            var cookieAccessToken = this.tokensProvider.readRequestAccessToken(request);
            var cookieRefreshToken = this.tokensProvider.readRequestRefreshToken(request);
            var user = this.tokensService.getJwtUserByAccessTokenOrThrow(cookieAccessToken, cookieRefreshToken);

            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            this.sessionRegistry.register(new Session(user.username(), cookieAccessToken.getJwtAccessToken(), cookieRefreshToken.getJwtRefreshToken()));

            this.jwtTokensFilterExtension.doFilter(request, response, filterChain);
        } catch (AccessTokenNotFoundException | AccessTokenExpiredException ex) {
            LOGGER.error("JWT tokens filter, access token is required. Message: {}", ex.getMessage());
            // NOTE: place to refresh token. problem how to distinguish authenticated vs. anonymous/permitAll endpoints
            filterChain.doFilter(request, response);
        } catch (RefreshTokenNotFoundException | AccessTokenInvalidException |
                 RefreshTokenInvalidException | AccessTokenDbNotFoundException ex) {
            LOGGER.error("JWT tokens filter, clear cookies. Message: {}", ex.getMessage());
            this.tokensProvider.clearTokens(response);
            response.sendError(HttpStatus.UNAUTHORIZED.value());
        }
    }
}
