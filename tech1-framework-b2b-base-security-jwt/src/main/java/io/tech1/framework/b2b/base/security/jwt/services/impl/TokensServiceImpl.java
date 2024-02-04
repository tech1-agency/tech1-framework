package io.tech1.framework.b2b.base.security.jwt.services.impl;

import io.tech1.framework.b2b.base.security.jwt.assistants.userdetails.JwtUserDetailsService;
import io.tech1.framework.b2b.base.security.jwt.tokens.facade.TokensProvider;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseRefreshTokens;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersSessionsService;
import io.tech1.framework.b2b.base.security.jwt.services.TokensContextThrowerService;
import io.tech1.framework.b2b.base.security.jwt.services.TokensService;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityJwtTokenUtils;
import io.tech1.framework.domain.exceptions.tokens.*;
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
    // Tokens
    private final TokensProvider tokensProvider;
    // Utilities
    private final SecurityJwtTokenUtils securityJwtTokenUtils;

    @Override
    public JwtUser getJwtUserByAccessTokenOrThrow(
            RequestAccessToken requestAccessToken,
            RequestRefreshToken requestRefreshToken
    ) throws AccessTokenInvalidException, RefreshTokenInvalidException, AccessTokenExpiredException, AccessTokenDbNotFoundException {
        var accessToken = requestAccessToken.getJwtAccessToken();
        var refreshToken = requestRefreshToken.getJwtRefreshToken();

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
    ) throws RefreshTokenNotFoundException, RefreshTokenInvalidException, RefreshTokenExpiredException, RefreshTokenDbNotFoundException {
        var oldRefreshToken = this.tokensProvider.readRequestRefreshToken(request).getJwtRefreshToken();

        var refreshTokenValidatedClaims = this.tokensContextThrowerService.verifyValidityOrThrow(oldRefreshToken);
        this.tokensContextThrowerService.verifyRefreshTokenExpirationOrThrow(refreshTokenValidatedClaims);
        var refreshTokenValidatedTuple = this.tokensContextThrowerService.verifyDbPresenceOrThrow(oldRefreshToken, refreshTokenValidatedClaims);
        var user = refreshTokenValidatedTuple.a();
        var session = refreshTokenValidatedTuple.b();

        var accessToken = this.securityJwtTokenUtils.createJwtAccessToken(user.getJwtTokenCreationParams());
        var newRefreshToken = this.securityJwtTokenUtils.createJwtRefreshToken(user.getJwtTokenCreationParams());

        this.baseUsersSessionsService.refresh(user, session, accessToken, newRefreshToken, request);

        this.tokensProvider.createResponseAccessToken(accessToken, response);
        this.tokensProvider.createResponseRefreshToken(newRefreshToken, response);

        var username = user.username();
        LOGGER.debug("JWT refresh token operation was successfully completed. Username: {}", username);

        this.sessionRegistry.renew(username, oldRefreshToken, accessToken, newRefreshToken);

        return new ResponseRefreshTokens(accessToken, newRefreshToken);
    }
}
