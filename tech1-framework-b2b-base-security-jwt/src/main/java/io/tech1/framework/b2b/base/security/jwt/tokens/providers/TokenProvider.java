package io.tech1.framework.b2b.base.security.jwt.tokens.providers;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestRefreshToken;
import io.tech1.framework.domain.exceptions.tokens.AccessTokenNotFoundException;
import io.tech1.framework.domain.exceptions.tokens.RefreshTokenNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TokenProvider {
    void createResponseAccessToken(JwtAccessToken jwtAccessToken, HttpServletResponse response);
    void createResponseRefreshToken(JwtRefreshToken jwtRefreshToken, HttpServletResponse response);
    RequestAccessToken readRequestAccessToken(HttpServletRequest request) throws AccessTokenNotFoundException;
    RequestAccessToken readRequestAccessTokenOnWebsocketHandshake(HttpServletRequest request) throws AccessTokenNotFoundException;
    RequestRefreshToken readRequestRefreshToken(HttpServletRequest request) throws RefreshTokenNotFoundException;
    RequestRefreshToken readRequestRefreshTokenOnWebsocketHandshake(HttpServletRequest request) throws RefreshTokenNotFoundException;
    void clearTokens(HttpServletResponse response);
}
