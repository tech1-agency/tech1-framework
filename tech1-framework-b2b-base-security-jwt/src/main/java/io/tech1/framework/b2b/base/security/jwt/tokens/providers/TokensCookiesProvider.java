package io.tech1.framework.b2b.base.security.jwt.tokens.providers;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.tokens.facade.TokensProvider;
import io.tech1.framework.domain.exceptions.cookies.CookieNotFoundException;
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

import static io.tech1.framework.domain.utilities.http.HttpCookieUtility.*;
import static io.tech1.framework.domain.utilities.http.HttpCookieUtility.createNullCookie;
import static io.tech1.framework.domain.utilities.numbers.LongUtility.toIntExactOrZeroOnOverflow;

@Slf4j
@Service
@Qualifier("tokensCookiesProvider")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokensCookiesProvider implements TokensProvider {

    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public void createResponseAccessToken(JwtAccessToken jwtAccessToken, HttpServletResponse response) {
        var securityJwtConfigs = this.applicationFrameworkProperties.getSecurityJwtConfigs();
        var accessTokenConfiguration = securityJwtConfigs.getJwtTokensConfigs().getAccessToken();
        var jwtAccessTokenCookieCreationLatency = securityJwtConfigs.getCookiesConfigs().getJwtAccessTokenCookieCreationLatency();
        var maxAge = accessTokenConfiguration.getExpiration().getTimeAmount().toSeconds() - jwtAccessTokenCookieCreationLatency.getTimeAmount().toSeconds();

        var cookie =  createCookie(
                accessTokenConfiguration.getCookieKey(),
                jwtAccessToken.value(),
                securityJwtConfigs.getCookiesConfigs().getDomain(),
                true,
                toIntExactOrZeroOnOverflow(maxAge)
        );

        response.addCookie(cookie);
    }

    @Override
    public void createResponseRefreshToken(JwtRefreshToken jwtRefreshToken, HttpServletResponse response) {
        var securityJwtConfigs = this.applicationFrameworkProperties.getSecurityJwtConfigs();
        var refreshTokenConfiguration = securityJwtConfigs.getJwtTokensConfigs().getRefreshToken();

        var cookie = createCookie(
                refreshTokenConfiguration.getCookieKey(),
                jwtRefreshToken.value(),
                securityJwtConfigs.getCookiesConfigs().getDomain(),
                true,
                toIntExactOrZeroOnOverflow(refreshTokenConfiguration.getExpiration().getTimeAmount().toSeconds())
        );

        response.addCookie(cookie);
    }

    @Override
    public RequestAccessToken readRequestAccessToken(HttpServletRequest request) throws AccessTokenNotFoundException {
        try {
            var accessToken = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getAccessToken();
            var cookie = readCookie(request, accessToken.getCookieKey());
            return new RequestAccessToken(cookie);
        } catch (CookieNotFoundException ex) {
            throw new AccessTokenNotFoundException();
        }
    }

    @Override
    public RequestRefreshToken readRequestRefreshToken(HttpServletRequest request) throws RefreshTokenNotFoundException {
        try {
            var refreshToken = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getRefreshToken();
            var cookie = readCookie(request, refreshToken.getCookieKey());
            return new RequestRefreshToken(cookie);
        } catch (CookieNotFoundException ex) {
            throw new RefreshTokenNotFoundException();
        }
    }

    @Override
    public void clearTokens(HttpServletResponse response) {
        var securityJwtConfigs = this.applicationFrameworkProperties.getSecurityJwtConfigs();
        var cookiesConfigs = securityJwtConfigs.getCookiesConfigs();
        var accessToken = securityJwtConfigs.getJwtTokensConfigs().getAccessToken();
        var refreshToken = securityJwtConfigs.getJwtTokensConfigs().getRefreshToken();

        response.addCookie(createNullCookie(accessToken.getCookieKey(), cookiesConfigs.getDomain()));
        response.addCookie(createNullCookie(refreshToken.getCookieKey(), cookiesConfigs.getDomain()));
    }
}
