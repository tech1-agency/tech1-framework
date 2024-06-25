package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.assistants.userdetails.JwtUserDetailsService;
import io.tech1.framework.b2b.base.security.jwt.domain.db.UserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtTokenValidatedClaims;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.repositories.UsersSessionsRepository;
import io.tech1.framework.b2b.base.security.jwt.services.TokensContextThrowerService;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityJwtTokenUtils;
import io.tech1.framework.foundation.domain.exceptions.tokens.*;
import io.tech1.framework.foundation.domain.tuples.Tuple2;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractTokensContextThrowerService implements TokensContextThrowerService {

    // Assistants
    protected final JwtUserDetailsService jwtUserDetailsService;
    // Repositories
    protected final UsersSessionsRepository usersSessionsRepository;
    // Utilities
    protected final SecurityJwtTokenUtils securityJwtTokenUtils;

    @Override
    public JwtTokenValidatedClaims verifyValidityOrThrow(JwtAccessToken accessToken) throws AccessTokenInvalidException {
        var validatedClaims = this.securityJwtTokenUtils.validate(accessToken);
        if (validatedClaims.isInvalid()) {
            SecurityContextHolder.clearContext();
            throw new AccessTokenInvalidException();
        }
        return validatedClaims;
    }

    @Override
    public JwtTokenValidatedClaims verifyValidityOrThrow(JwtRefreshToken refreshToken) throws RefreshTokenInvalidException {
        var validatedClaims = this.securityJwtTokenUtils.validate(refreshToken);
        if (validatedClaims.isInvalid()) {
            SecurityContextHolder.clearContext();
            throw new RefreshTokenInvalidException();
        }
        return validatedClaims;
    }

    @Override
    public void verifyAccessTokenExpirationOrThrow(JwtTokenValidatedClaims validatedClaims) throws AccessTokenExpiredException {
        if (validatedClaims.isExpired() && validatedClaims.isAccess()) {
            SecurityContextHolder.clearContext();
            throw new AccessTokenExpiredException(validatedClaims.username());
        }
    }

    @Override
    public void verifyRefreshTokenExpirationOrThrow(JwtTokenValidatedClaims validatedClaims) throws RefreshTokenExpiredException {
        if (validatedClaims.isExpired() && validatedClaims.isRefresh()) {
            SecurityContextHolder.clearContext();
            throw new RefreshTokenExpiredException(validatedClaims.username());
        }
    }

    @Override
    public void verifyDbPresenceOrThrow(JwtAccessToken accessToken, JwtTokenValidatedClaims validatedClaims) throws AccessTokenDbNotFoundException {
        var username = validatedClaims.username();
        var databasePresence = this.usersSessionsRepository.isPresent(accessToken);
        if (!databasePresence.present()) {
            SecurityContextHolder.clearContext();
            throw new AccessTokenDbNotFoundException(username);
        }
    }

    @Override
    public Tuple2<JwtUser, UserSession> verifyDbPresenceOrThrow(JwtRefreshToken refreshToken, JwtTokenValidatedClaims validatedClaims) throws RefreshTokenDbNotFoundException {
        var username = validatedClaims.username();
        var databasePresence = this.usersSessionsRepository.isPresent(refreshToken);
        if (!databasePresence.present()) {
            SecurityContextHolder.clearContext();
            throw new RefreshTokenDbNotFoundException(username);
        }
        var user = this.jwtUserDetailsService.loadUserByUsername(username.value());
        return new Tuple2<>(user, databasePresence.value());
    }
}
