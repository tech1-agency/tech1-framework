package io.tech1.framework.b2b.base.security.jwt.tokens.facade.impl;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.tokens.facade.TokensProvider;
import io.tech1.framework.b2b.base.security.jwt.tokens.providers.TokenCookiesProvider;
import io.tech1.framework.b2b.base.security.jwt.tokens.providers.TokenHeadersProvider;
import io.tech1.framework.domain.exceptions.tokens.AccessTokenNotFoundException;
import io.tech1.framework.domain.exceptions.tokens.CsrfTokenNotFoundException;
import io.tech1.framework.domain.exceptions.tokens.RefreshTokenNotFoundException;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.csrf.DefaultCsrfToken;
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
    public DefaultCsrfToken readCsrfToken(HttpServletRequest httpRequest) throws CsrfTokenNotFoundException {
        if (this.isCookiesProviderEnabled()) {
            return this.tokensCookiesProvider.readCsrfToken(httpRequest);
        } else {
            return this.tokensHeadersProvider.readCsrfToken(httpRequest);
        }
    }

    @Override
    public RequestAccessToken readRequestAccessToken(HttpServletRequest httpRequest) throws AccessTokenNotFoundException {
        if (this.isCookiesProviderEnabled()) {
            return this.tokensCookiesProvider.readRequestAccessToken(httpRequest);
        } else {
            return this.tokensHeadersProvider.readRequestAccessToken(httpRequest);
        }
    }

    @Override
    public RequestAccessToken readRequestAccessTokenOnWebsocketHandshake(HttpServletRequest httpRequest) throws AccessTokenNotFoundException {
        if (this.isCookiesProviderEnabled()) {
            return this.tokensCookiesProvider.readRequestAccessTokenOnWebsocketHandshake(httpRequest);
        } else {
            return this.tokensHeadersProvider.readRequestAccessTokenOnWebsocketHandshake(httpRequest);
        }
    }

    @Override
    public RequestRefreshToken readRequestRefreshToken(HttpServletRequest httpRequest) throws RefreshTokenNotFoundException {
        if (this.isCookiesProviderEnabled()) {
            return this.tokensCookiesProvider.readRequestRefreshToken(httpRequest);
        } else {
            return this.tokensHeadersProvider.readRequestRefreshToken(httpRequest);
        }
    }

    @Override
    public RequestRefreshToken readRequestRefreshTokenOnWebsocketHandshake(HttpServletRequest httpRequest) throws RefreshTokenNotFoundException {
        if (this.isCookiesProviderEnabled()) {
            return this.tokensCookiesProvider.readRequestRefreshTokenOnWebsocketHandshake(httpRequest);
        } else {
            return this.tokensHeadersProvider.readRequestRefreshTokenOnWebsocketHandshake(httpRequest);
        }
    }

    @Override
    public void clearTokens(HttpServletResponse httpResponse) {
        if (this.isCookiesProviderEnabled()) {
            this.tokensCookiesProvider.clearTokens(httpResponse);
        } else {
            this.tokensHeadersProvider.clearTokens(httpResponse);
        }
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    public boolean isCookiesProviderEnabled() {
        return this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getStorageMethod().isCookies();
    }
}
