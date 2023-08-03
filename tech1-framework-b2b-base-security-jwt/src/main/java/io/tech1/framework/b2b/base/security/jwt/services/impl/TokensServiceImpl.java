package io.tech1.framework.b2b.base.security.jwt.services.impl;

import io.tech1.framework.b2b.base.security.jwt.assistants.userdetails.JwtUserDetailsService;
import io.tech1.framework.b2b.base.security.jwt.cookies.CookieProvider;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseRefreshTokens;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersSessionsService;
import io.tech1.framework.b2b.base.security.jwt.services.TokensContextThrowerService;
import io.tech1.framework.b2b.base.security.jwt.services.TokensService;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityJwtTokenUtils;
import io.tech1.framework.domain.exceptions.cookie.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokensServiceImpl implements TokensService {

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

    @Override
    public JwtUser getJwtUserByAccessTokenOrThrow(
            CookieAccessToken cookieAccessToken,
            CookieRefreshToken cookieRefreshToken
    ) throws CookieAccessTokenInvalidException, CookieRefreshTokenInvalidException, CookieAccessTokenExpiredException, CookieAccessTokenDbNotFoundException {
        var accessToken = cookieAccessToken.getJwtAccessToken();
        var refreshToken = cookieRefreshToken.getJwtRefreshToken();

        var accessTokenValidatedClaims = this.tokensContextThrowerService.verifyValidityOrThrow(accessToken);
        this.tokensContextThrowerService.verifyValidityOrThrow(refreshToken);

        this.tokensContextThrowerService.verifyAccessTokenExpirationOrThrow(accessTokenValidatedClaims);
        this.tokensContextThrowerService.verifyDbPresenceOrThrow(accessToken, accessTokenValidatedClaims);

        // JWT Access Token: isValid + isAlive
        return this.jwtUserDetailsService.loadUserByUsername(accessTokenValidatedClaims.username().identifier());
    }

    @Override
    public ResponseRefreshTokens refreshSessionOrThrow(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws CookieRefreshTokenNotFoundException, CookieRefreshTokenInvalidException, CookieRefreshTokenExpiredException, CookieRefreshTokenDbNotFoundException {
        var oldRefreshToken = this.cookieProvider.readJwtRefreshToken(request).getJwtRefreshToken();

        var refreshTokenValidatedClaims = this.tokensContextThrowerService.verifyValidityOrThrow(oldRefreshToken);
        this.tokensContextThrowerService.verifyRefreshTokenExpirationOrThrow(refreshTokenValidatedClaims);
        // TODO [YY] tuple.a() -> tuple usage
        var user = this.tokensContextThrowerService.verifyDbPresenceOrThrow(oldRefreshToken, refreshTokenValidatedClaims).a();

        var accessToken = this.securityJwtTokenUtils.createJwtAccessToken(user.getJwtTokenCreationParams());
        var newRefreshToken = this.securityJwtTokenUtils.createJwtRefreshToken(user.getJwtTokenCreationParams());

        this.baseUsersSessionsService.refresh(user, oldRefreshToken, accessToken, newRefreshToken, request);

        this.cookieProvider.createJwtAccessCookie(accessToken, response);
        this.cookieProvider.createJwtRefreshCookie(newRefreshToken, response);

        var username = user.username();
        LOGGER.debug("JWT refresh token operation was successfully completed. Username: {}", username);

        this.sessionRegistry.renew(username, oldRefreshToken, accessToken, newRefreshToken);

        return new ResponseRefreshTokens(accessToken, newRefreshToken);
    }
}
