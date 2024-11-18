package jbst.iam.services;

import jbst.iam.domain.db.UserSession;
import jbst.iam.domain.jwt.JwtAccessToken;
import jbst.iam.domain.jwt.JwtRefreshToken;
import jbst.iam.domain.jwt.JwtTokenValidatedClaims;
import jbst.iam.domain.jwt.JwtUser;
import tech1.framework.foundation.domain.exceptions.tokens.*;
import tech1.framework.foundation.domain.tuples.Tuple2;

public interface TokensContextThrowerService {
    JwtTokenValidatedClaims verifyValidityOrThrow(JwtAccessToken accessToken) throws AccessTokenInvalidException;
    JwtTokenValidatedClaims verifyValidityOrThrow(JwtRefreshToken refreshToken) throws RefreshTokenInvalidException;

    void verifyAccessTokenExpirationOrThrow(JwtTokenValidatedClaims validatedClaims) throws AccessTokenExpiredException;
    void verifyRefreshTokenExpirationOrThrow(JwtTokenValidatedClaims validatedClaims) throws RefreshTokenExpiredException;

    void verifyDbPresenceOrThrow(JwtAccessToken accessToken, JwtTokenValidatedClaims validatedClaims) throws AccessTokenDbNotFoundException;
    Tuple2<JwtUser, UserSession> verifyDbPresenceOrThrow(JwtRefreshToken refreshToken, JwtTokenValidatedClaims validatedClaims) throws RefreshTokenDbNotFoundException;
}
