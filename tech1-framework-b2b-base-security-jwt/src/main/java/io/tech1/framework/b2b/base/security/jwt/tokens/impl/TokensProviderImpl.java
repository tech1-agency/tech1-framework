package io.tech1.framework.b2b.base.security.jwt.tokens.impl;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.tokens.TokensProvider;
import io.tech1.framework.b2b.base.security.jwt.tokens.providers.TokensCookieProvider;
import io.tech1.framework.domain.exceptions.tokens.AccessTokenNotFoundException;
import io.tech1.framework.domain.exceptions.tokens.RefreshTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokensProviderImpl implements TokensProvider {

    // Properties
    private final TokensCookieProvider tokensCookieProvider;

    @Override
    public void createResponseAccessToken(JwtAccessToken jwtAccessToken, HttpServletResponse response) {
        this.tokensCookieProvider.createResponseAccessToken(jwtAccessToken, response);
    }

    @Override
    public void createResponseRefreshToken(JwtRefreshToken jwtRefreshToken, HttpServletResponse response) {
        this.tokensCookieProvider.createResponseRefreshToken(jwtRefreshToken, response);
    }

    @Override
    public RequestAccessToken readRequestAccessToken(HttpServletRequest request) throws AccessTokenNotFoundException {
        return this.tokensCookieProvider.readRequestAccessToken(request);
    }

    @Override
    public RequestRefreshToken readRequestRefreshToken(HttpServletRequest request) throws RefreshTokenNotFoundException {
        return this.tokensCookieProvider.readRequestRefreshToken(request);
    }

    @Override
    public void clearTokens(HttpServletResponse response) {
        this.tokensCookieProvider.clearTokens(response);
    }
}
