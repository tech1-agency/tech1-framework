package io.tech1.framework.b2b.mongodb.security.jwt.services;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtTokenValidatedClaims;
import io.tech1.framework.domain.exceptions.cookie.*;

public interface TokenContextThrowerService {
    JwtTokenValidatedClaims verifyValidityOrThrow(JwtAccessToken jwtAccessToken) throws CookieAccessTokenInvalidException;
    JwtTokenValidatedClaims verifyValidityOrThrow(JwtRefreshToken jwtRefreshToken) throws CookieRefreshTokenInvalidException;
    void verifyAccessTokenExpirationOrThrow(JwtTokenValidatedClaims validatedClaims) throws CookieAccessTokenExpiredException;
    void verifyRefreshTokenExpirationOrThrow(JwtTokenValidatedClaims validatedClaims) throws CookieRefreshTokenExpiredException;
    DbUser verifyDbPresenceOrThrow(JwtTokenValidatedClaims validatedClaims, JwtRefreshToken oldJwtRefreshToken) throws CookieRefreshTokenDbNotFoundException;
}
