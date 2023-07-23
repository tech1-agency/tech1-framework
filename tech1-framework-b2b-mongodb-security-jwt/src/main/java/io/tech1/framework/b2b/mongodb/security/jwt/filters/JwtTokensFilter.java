package io.tech1.framework.b2b.mongodb.security.jwt.filters;

import io.tech1.framework.b2b.mongodb.security.jwt.cookies.CookieProvider;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.session.Session;
import io.tech1.framework.b2b.mongodb.security.jwt.services.TokenService;
import io.tech1.framework.b2b.mongodb.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.domain.exceptions.cookie.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtTokensFilter extends OncePerRequestFilter {

    // Session
    private final SessionRegistry sessionRegistry;
    // Services
    private final TokenService tokenService;
    // Cookies
    private final CookieProvider cookieProvider;

    @SuppressWarnings("NullableProblems")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            var tuple2 = this.tokenService.getJwtUserByAccessTokenOrThrow(request);
            var currentJwtUser = tuple2.a();

            var authentication = new UsernamePasswordAuthenticationToken(currentJwtUser, null, currentJwtUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Session Registry
            this.sessionRegistry.register(
                    new Session(
                            currentJwtUser.getDbUser().getUsername(),
                            tuple2.b()
                    )
            );

            filterChain.doFilter(request, response);
        } catch (CookieAccessTokenNotFoundException | CookieAccessTokenExpiredException ex) {
            LOGGER.warn("JWT tokens filter, access token is required. Message: {}", ex.getMessage());
            // NOTE: place to refresh token. problem how to distinguish authenticated vs. anonymous/permitAll endpoints
            filterChain.doFilter(request, response);
        } catch (CookieAccessTokenInvalidException | CookieRefreshTokenInvalidException | CookieRefreshTokenNotFoundException ex) {
            LOGGER.warn("JWT tokens filter, clear cookies. Message: {}", ex.getMessage());
            this.cookieProvider.clearCookies(response);
            response.sendError(HttpStatus.UNAUTHORIZED.value());
        }
    }
}
