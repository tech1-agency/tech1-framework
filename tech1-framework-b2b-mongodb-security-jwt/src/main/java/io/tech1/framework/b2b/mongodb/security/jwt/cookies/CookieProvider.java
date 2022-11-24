package io.tech1.framework.b2b.mongodb.security.jwt.cookies;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.exceptions.cookie.CookieAccessTokenNotFoundException;
import io.tech1.framework.domain.exceptions.cookie.CookieRefreshTokenNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CookieProvider {
    void createJwtAccessCookie(JwtAccessToken jwtAccessToken, HttpServletResponse httpServletResponse);
    void createJwtRefreshCookie(JwtRefreshToken jwtRefreshToken, HttpServletResponse httpServletResponse);
    CookieAccessToken readJwtAccessToken(HttpServletRequest httpServletRequest) throws CookieAccessTokenNotFoundException;
    CookieRefreshToken readJwtRefreshToken(HttpServletRequest httpServletRequest) throws CookieRefreshTokenNotFoundException;
    void clearCookies(HttpServletResponse httpServletResponse);
}
