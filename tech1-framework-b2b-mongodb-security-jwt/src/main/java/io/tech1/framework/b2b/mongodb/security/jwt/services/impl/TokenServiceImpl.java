package io.tech1.framework.b2b.mongodb.security.jwt.services.impl;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.sessions.Session;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.b2b.mongodb.security.jwt.assistants.userdetails.JwtUserDetailsAssistant;
import io.tech1.framework.b2b.mongodb.security.jwt.cookies.CookieProvider;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseUserSession1;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.mongodb.security.jwt.services.TokenContextThrowerService;
import io.tech1.framework.b2b.mongodb.security.jwt.services.TokenService;
import io.tech1.framework.b2b.mongodb.security.jwt.services.UserSessionService;
import io.tech1.framework.b2b.mongodb.security.jwt.utilities.SecurityJwtTokenUtility;
import io.tech1.framework.domain.exceptions.cookie.*;
import io.tech1.framework.domain.tuples.Tuple2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenServiceImpl implements TokenService {

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

    @Override
    public Tuple2<JwtUser, JwtRefreshToken> getJwtUserByAccessTokenOrThrow(
            CookieAccessToken cookieAccessToken,
            CookieRefreshToken cookieRefreshToken
    ) throws CookieAccessTokenInvalidException, CookieRefreshTokenInvalidException, CookieAccessTokenExpiredException {

        var jwtAccessToken = cookieAccessToken.getJwtAccessToken();
        var jwtRefreshToken = cookieRefreshToken.getJwtRefreshToken();

        var accessTokenValidatedClaims = this.tokenContextThrowerService.verifyValidityOrThrow(jwtAccessToken);
        var refreshTokenValidatedClaims = this.tokenContextThrowerService.verifyValidityOrThrow(jwtRefreshToken);

        this.tokenContextThrowerService.verifyAccessTokenExpirationOrThrow(accessTokenValidatedClaims);

        // JWT Access Token: isValid + isAlive
        var jwtUser = this.jwtUserDetailsAssistant.loadUserByUsername(accessTokenValidatedClaims.safeGetUsername().identifier());
        return new Tuple2<>(jwtUser, new JwtRefreshToken(refreshTokenValidatedClaims.jwtToken()));
    }

    @Override
    public ResponseUserSession1 refreshSessionOrThrow(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws CookieRefreshTokenNotFoundException, CookieRefreshTokenInvalidException, CookieRefreshTokenExpiredException, CookieRefreshTokenDbNotFoundException {
        var oldCookieRefreshToken = this.cookieProvider.readJwtRefreshToken(request);
        var oldJwtRefreshToken = oldCookieRefreshToken.getJwtRefreshToken();

        var refreshTokenValidatedClaims = this.tokenContextThrowerService.verifyValidityOrThrow(oldJwtRefreshToken);
        this.tokenContextThrowerService.verifyRefreshTokenExpirationOrThrow(refreshTokenValidatedClaims);
        var user = this.tokenContextThrowerService.verifyDbPresenceOrThrow(refreshTokenValidatedClaims, oldJwtRefreshToken);

        var jwtAccessToken = this.securityJwtTokenUtility.createJwtAccessToken(user.getJwtTokenCreationParams());
        var newJwtRefreshToken = this.securityJwtTokenUtility.createJwtRefreshToken(user.getJwtTokenCreationParams());

        var userSession = this.userSessionService.refresh(user, oldJwtRefreshToken, newJwtRefreshToken, request);

        this.cookieProvider.createJwtAccessCookie(jwtAccessToken, response);
        this.cookieProvider.createJwtRefreshCookie(newJwtRefreshToken, response);

        var username = user.getUsername();
        LOGGER.debug("JWT refresh token operation was successfully completed. Username: {}", username);

        this.sessionRegistry.renew(
                new Session(
                        username,
                        new JwtRefreshToken(oldCookieRefreshToken.value())
                ),
                new Session(
                        username,
                        newJwtRefreshToken
                )
        );

        return new ResponseUserSession1(userSession.getJwtRefreshToken().value());
    }
}
