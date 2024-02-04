package io.tech1.framework.b2b.base.security.jwt.cookies;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.exceptions.tokens.AccessTokenNotFoundException;
import io.tech1.framework.domain.exceptions.tokens.RefreshTokenNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CookieProvider {
    void createJwtAccessCookie(JwtAccessToken jwtAccessToken, HttpServletResponse httpServletResponse);
    void createJwtRefreshCookie(JwtRefreshToken jwtRefreshToken, HttpServletResponse httpServletResponse);
    CookieAccessToken readJwtAccessToken(HttpServletRequest httpServletRequest) throws AccessTokenNotFoundException;
    CookieRefreshToken readJwtRefreshToken(HttpServletRequest httpServletRequest) throws RefreshTokenNotFoundException;
    void clearCookies(HttpServletResponse httpServletResponse);
}
