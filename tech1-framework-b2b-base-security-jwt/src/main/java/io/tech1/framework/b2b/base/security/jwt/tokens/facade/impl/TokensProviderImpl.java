package io.tech1.framework.b2b.base.security.jwt.tokens.facade.impl;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.tokens.facade.TokensProvider;
import io.tech1.framework.b2b.base.security.jwt.tokens.providers.TokenCookiesProvider;
import io.tech1.framework.b2b.base.security.jwt.tokens.providers.TokenHeadersProvider;
import io.tech1.framework.domain.exceptions.tokens.AccessTokenNotFoundException;
import io.tech1.framework.domain.exceptions.tokens.RefreshTokenNotFoundException;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
public class TokensProviderImpl implements TokensProvider {

    // Providers
    private final TokenCookiesProvider tokensCookiesProvider;
    private final TokenHeadersProvider tokensHeadersProvider;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Autowired
    public TokensProviderImpl(
            @Qualifier("tokenCookiesProvider") TokenCookiesProvider tokensCookiesProvider,
            @Qualifier("tokenHeadersProvider") TokenHeadersProvider tokensHeadersProvider,
            ApplicationFrameworkProperties applicationFrameworkProperties
    ) {
        this.tokensCookiesProvider = tokensCookiesProvider;
        this.tokensHeadersProvider = tokensHeadersProvider;
        this.applicationFrameworkProperties = applicationFrameworkProperties;
    }

    @Override
    public void createResponseAccessToken(JwtAccessToken jwtAccessToken, HttpServletResponse response) {
        if (this.isCookiesProviderEnabled()) {
            this.tokensCookiesProvider.createResponseAccessToken(jwtAccessToken, response);
        } else {
            this.tokensHeadersProvider.createResponseAccessToken(jwtAccessToken, response);
        }
    }

    @Override
    public void createResponseRefreshToken(JwtRefreshToken jwtRefreshToken, HttpServletResponse response) {
        if (this.isCookiesProviderEnabled()) {
            this.tokensCookiesProvider.createResponseRefreshToken(jwtRefreshToken, response);
        } else {
            this.tokensHeadersProvider.createResponseRefreshToken(jwtRefreshToken, response);
        }
    }

    @Override
    public RequestAccessToken readRequestAccessToken(HttpServletRequest request) throws AccessTokenNotFoundException {
        if (this.isCookiesProviderEnabled()) {
            return this.tokensCookiesProvider.readRequestAccessToken(request);
        } else {
            return this.tokensHeadersProvider.readRequestAccessToken(request);
        }
    }

    @Override
    public RequestAccessToken readRequestAccessTokenOnWebsocketHandshake(HttpServletRequest request) throws AccessTokenNotFoundException {
        if (this.isCookiesProviderEnabled()) {
            return this.tokensCookiesProvider.readRequestAccessTokenOnWebsocketHandshake(request);
        } else {
            return this.tokensHeadersProvider.readRequestAccessTokenOnWebsocketHandshake(request);
        }
    }

    @Override
    public RequestRefreshToken readRequestRefreshToken(HttpServletRequest request) throws RefreshTokenNotFoundException {
        if (this.isCookiesProviderEnabled()) {
            return this.tokensCookiesProvider.readRequestRefreshToken(request);
        } else {
            return this.tokensHeadersProvider.readRequestRefreshToken(request);
        }
    }

    @Override
    public RequestRefreshToken readRequestRefreshTokenOnWebsocketHandshake(HttpServletRequest request) throws RefreshTokenNotFoundException {
        if (this.isCookiesProviderEnabled()) {
            return this.tokensCookiesProvider.readRequestRefreshTokenOnWebsocketHandshake(request);
        } else {
            return this.tokensHeadersProvider.readRequestRefreshTokenOnWebsocketHandshake(request);
        }
    }

    @Override
    public void clearTokens(HttpServletResponse response) {
        if (this.isCookiesProviderEnabled()) {
            this.tokensCookiesProvider.clearTokens(response);
        } else {
            this.tokensHeadersProvider.clearTokens(response);
        }
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    public boolean isCookiesProviderEnabled() {
        return this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getStorageMethod().isCookies();
    }
}
