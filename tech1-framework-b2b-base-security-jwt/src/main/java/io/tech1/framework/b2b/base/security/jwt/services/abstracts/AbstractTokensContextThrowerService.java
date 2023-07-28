package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtTokenValidatedClaims;
import io.tech1.framework.b2b.base.security.jwt.services.TokensContextThrowerService;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityJwtTokenUtils;
import io.tech1.framework.domain.exceptions.cookie.CookieAccessTokenExpiredException;
import io.tech1.framework.domain.exceptions.cookie.CookieAccessTokenInvalidException;
import io.tech1.framework.domain.exceptions.cookie.CookieRefreshTokenExpiredException;
import io.tech1.framework.domain.exceptions.cookie.CookieRefreshTokenInvalidException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

@AllArgsConstructor
public abstract class AbstractTokensContextThrowerService implements TokensContextThrowerService {

    // Utilities
    protected final SecurityJwtTokenUtils securityJwtTokenUtils;

    @Override
    public JwtTokenValidatedClaims verifyValidityOrThrow(JwtAccessToken jwtAccessToken) throws CookieAccessTokenInvalidException {
        var validatedClaims = this.securityJwtTokenUtils.validate(jwtAccessToken);
        if (validatedClaims.isInvalid()) {
            SecurityContextHolder.clearContext();
            throw new CookieAccessTokenInvalidException();
        }
        return validatedClaims;
    }

    @Override
    public JwtTokenValidatedClaims verifyValidityOrThrow(JwtRefreshToken jwtRefreshToken) throws CookieRefreshTokenInvalidException {
        var validatedClaims = this.securityJwtTokenUtils.validate(jwtRefreshToken);
        if (validatedClaims.isInvalid()) {
            SecurityContextHolder.clearContext();
            throw new CookieRefreshTokenInvalidException();
        }
        return validatedClaims;
    }

    @Override
    public void verifyAccessTokenExpirationOrThrow(JwtTokenValidatedClaims validatedClaims) throws CookieAccessTokenExpiredException {
        if (this.securityJwtTokenUtils.isExpired(validatedClaims) && validatedClaims.isAccess()) {
            SecurityContextHolder.clearContext();
            throw new CookieAccessTokenExpiredException(validatedClaims.safeGetUsername());
        }
    }

    @Override
    public void verifyRefreshTokenExpirationOrThrow(JwtTokenValidatedClaims validatedClaims) throws CookieRefreshTokenExpiredException {
        if (this.securityJwtTokenUtils.isExpired(validatedClaims) && validatedClaims.isRefresh()) {
            SecurityContextHolder.clearContext();
            throw new CookieRefreshTokenExpiredException(validatedClaims.safeGetUsername());
        }
    }
}
