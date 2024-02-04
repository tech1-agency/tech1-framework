package io.tech1.framework.b2b.base.security.jwt.tokens.facade.impl;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.tokens.facade.TokensProvider;
import io.tech1.framework.domain.exceptions.tokens.AccessTokenNotFoundException;
import io.tech1.framework.domain.exceptions.tokens.RefreshTokenNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
@Qualifier("tokensProvider")
public class TokensProviderImpl implements TokensProvider {

    // Providers
    private final TokensProvider tokensCookiesProvider;
    private final TokensProvider tokensHeadersProvider;

    @Autowired
    public TokensProviderImpl(
            @Qualifier("tokensCookiesProvider") TokensProvider tokensCookiesProvider,
            @Qualifier("tokensHeadersProvider") TokensProvider tokensHeadersProvider
    ) {
        this.tokensCookiesProvider = tokensCookiesProvider;
        this.tokensHeadersProvider = tokensHeadersProvider;
    }

    @Override
    public void createResponseAccessToken(JwtAccessToken jwtAccessToken, HttpServletResponse response) {
        this.tokensCookiesProvider.createResponseAccessToken(jwtAccessToken, response);
    }

    @Override
    public void createResponseRefreshToken(JwtRefreshToken jwtRefreshToken, HttpServletResponse response) {
        this.tokensCookiesProvider.createResponseRefreshToken(jwtRefreshToken, response);
    }

    @Override
    public RequestAccessToken readRequestAccessToken(HttpServletRequest request) throws AccessTokenNotFoundException {
        return this.tokensCookiesProvider.readRequestAccessToken(request);
    }

    @Override
    public RequestRefreshToken readRequestRefreshToken(HttpServletRequest request) throws RefreshTokenNotFoundException {
        return this.tokensCookiesProvider.readRequestRefreshToken(request);
    }

    @Override
    public void clearTokens(HttpServletResponse response) {
        this.tokensCookiesProvider.clearTokens(response);
    }
}
