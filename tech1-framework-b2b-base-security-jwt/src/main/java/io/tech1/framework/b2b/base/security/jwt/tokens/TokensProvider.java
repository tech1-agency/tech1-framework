package io.tech1.framework.b2b.base.security.jwt.tokens;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.exceptions.tokens.AccessTokenNotFoundException;
import io.tech1.framework.domain.exceptions.tokens.RefreshTokenNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TokensProvider {
    void createJwtAccessToken(JwtAccessToken jwtAccessToken, HttpServletResponse response);
    void createJwtRefreshToken(JwtRefreshToken jwtRefreshToken, HttpServletResponse response);
    CookieAccessToken readJwtAccessToken(HttpServletRequest request) throws AccessTokenNotFoundException;
    CookieRefreshToken readJwtRefreshToken(HttpServletRequest request) throws RefreshTokenNotFoundException;
    void clearTokens(HttpServletResponse response);
}
