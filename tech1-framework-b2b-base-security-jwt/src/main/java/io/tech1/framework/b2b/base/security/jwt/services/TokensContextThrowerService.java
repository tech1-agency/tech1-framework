package io.tech1.framework.b2b.base.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtTokenValidatedClaims;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.domain.exceptions.cookie.*;

public interface TokensContextThrowerService {
    JwtTokenValidatedClaims verifyValidityOrThrow(JwtAccessToken accessToken) throws CookieAccessTokenInvalidException;
    JwtTokenValidatedClaims verifyValidityOrThrow(JwtRefreshToken refreshToken) throws CookieRefreshTokenInvalidException;

    void verifyAccessTokenExpirationOrThrow(JwtTokenValidatedClaims validatedClaims) throws CookieAccessTokenExpiredException;
    void verifyRefreshTokenExpirationOrThrow(JwtTokenValidatedClaims validatedClaims) throws CookieRefreshTokenExpiredException;

    void verifyDbPresenceOrThrow(JwtAccessToken accessToken, JwtTokenValidatedClaims validatedClaims) throws CookieAccessTokenDbNotFoundException;
    JwtUser verifyDbPresenceOrThrow(JwtRefreshToken refreshToken, JwtTokenValidatedClaims validatedClaims) throws CookieRefreshTokenDbNotFoundException;
}
