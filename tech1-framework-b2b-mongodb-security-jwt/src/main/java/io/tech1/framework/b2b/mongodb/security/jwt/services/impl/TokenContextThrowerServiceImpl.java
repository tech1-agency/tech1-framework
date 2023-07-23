package io.tech1.framework.b2b.mongodb.security.jwt.services.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.assistants.userdetails.JwtUserDetailsAssistant;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtTokenValidatedClaims;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.UserSessionRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.services.TokenContextThrowerService;
import io.tech1.framework.b2b.mongodb.security.jwt.utilities.SecurityJwtTokenUtility;
import io.tech1.framework.domain.exceptions.cookie.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenContextThrowerServiceImpl implements TokenContextThrowerService {

    // Assistants
    private final JwtUserDetailsAssistant jwtUserDetailsAssistant;
    // Repositories
    private final UserSessionRepository userSessionRepository;
    // Utilities
    private final SecurityJwtTokenUtility securityJwtTokenUtility;

    @Override
    public JwtTokenValidatedClaims verifyValidityOrThrow(JwtAccessToken jwtAccessToken) throws CookieAccessTokenInvalidException {
        var validatedClaims = this.securityJwtTokenUtility.validate(jwtAccessToken);
        if (validatedClaims.isInvalid()) {
            SecurityContextHolder.clearContext();
            throw new CookieAccessTokenInvalidException();
        }
        return validatedClaims;
    }

    @Override
    public JwtTokenValidatedClaims verifyValidityOrThrow(JwtRefreshToken jwtRefreshToken) throws CookieRefreshTokenInvalidException {
        var validatedClaims = this.securityJwtTokenUtility.validate(jwtRefreshToken);
        if (validatedClaims.isInvalid()) {
            SecurityContextHolder.clearContext();
            throw new CookieRefreshTokenInvalidException();
        }
        return validatedClaims;
    }

    @Override
    public void verifyAccessTokenExpirationOrThrow(JwtTokenValidatedClaims validatedClaims) throws CookieAccessTokenExpiredException {
        if (this.securityJwtTokenUtility.isExpired(validatedClaims) && validatedClaims.isAccess()) {
            SecurityContextHolder.clearContext();
            throw new CookieAccessTokenExpiredException(validatedClaims.safeGetUsername());
        }
    }

    @Override
    public void verifyRefreshTokenExpirationOrThrow(JwtTokenValidatedClaims validatedClaims) throws CookieRefreshTokenExpiredException {
        if (this.securityJwtTokenUtility.isExpired(validatedClaims) && validatedClaims.isRefresh()) {
            SecurityContextHolder.clearContext();
            throw new CookieRefreshTokenExpiredException(validatedClaims.safeGetUsername());
        }
    }

    @Override
    public DbUser verifyDbPresenceOrThrow(JwtTokenValidatedClaims validatedClaims, JwtRefreshToken oldJwtRefreshToken) throws CookieRefreshTokenDbNotFoundException {
        var username = validatedClaims.safeGetUsername();
        var jwtUser = this.jwtUserDetailsAssistant.loadUserByUsername(username.identifier());
        var dbUser = jwtUser.getDbUser();
        var databasePresence = this.userSessionRepository.isPresent(oldJwtRefreshToken);
        if (!databasePresence) {
            SecurityContextHolder.clearContext();
            throw new CookieRefreshTokenDbNotFoundException(username);
        }
        return dbUser;
    }
}
