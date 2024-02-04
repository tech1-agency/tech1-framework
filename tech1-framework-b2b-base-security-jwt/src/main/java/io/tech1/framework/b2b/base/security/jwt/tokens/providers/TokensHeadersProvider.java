package io.tech1.framework.b2b.base.security.jwt.tokens.providers;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.tokens.facade.TokensProvider;
import io.tech1.framework.domain.exceptions.tokens.AccessTokenNotFoundException;
import io.tech1.framework.domain.exceptions.tokens.RefreshTokenNotFoundException;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
@Qualifier("tokensHeadersProvider")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokensHeadersProvider implements TokensProvider {

    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public void createResponseAccessToken(JwtAccessToken jwtAccessToken, HttpServletResponse response) {
        // ignored
    }

    @Override
    public void createResponseRefreshToken(JwtRefreshToken jwtRefreshToken, HttpServletResponse response) {
        // ignored
    }

    @Override
    public RequestAccessToken readRequestAccessToken(HttpServletRequest request) throws AccessTokenNotFoundException {
        // ignored
        return RequestAccessToken.random();
    }

    @Override
    public RequestRefreshToken readRequestRefreshToken(HttpServletRequest request) throws RefreshTokenNotFoundException {
        // ignored
        return RequestRefreshToken.random();
    }

    @Override
    public void clearTokens(HttpServletResponse response) {
        // ignored
    }
}
