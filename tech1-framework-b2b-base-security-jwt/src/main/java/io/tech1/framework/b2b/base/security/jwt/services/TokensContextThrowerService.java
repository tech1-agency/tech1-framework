package io.tech1.framework.b2b.base.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.domain.db.UserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtTokenValidatedClaims;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.domain.exceptions.tokens.*;
import io.tech1.framework.domain.tuples.Tuple2;

public interface TokensContextThrowerService {
    JwtTokenValidatedClaims verifyValidityOrThrow(JwtAccessToken accessToken) throws AccessTokenInvalidException;
    JwtTokenValidatedClaims verifyValidityOrThrow(JwtRefreshToken refreshToken) throws RefreshTokenInvalidException;

    void verifyAccessTokenExpirationOrThrow(JwtTokenValidatedClaims validatedClaims) throws AccessTokenExpiredException;
    void verifyRefreshTokenExpirationOrThrow(JwtTokenValidatedClaims validatedClaims) throws RefreshTokenExpiredException;

    void verifyDbPresenceOrThrow(JwtAccessToken accessToken, JwtTokenValidatedClaims validatedClaims) throws AccessTokenDbNotFoundException;
    Tuple2<JwtUser, UserSession> verifyDbPresenceOrThrow(JwtRefreshToken refreshToken, JwtTokenValidatedClaims validatedClaims) throws RefreshTokenDbNotFoundException;
}
