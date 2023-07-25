package io.tech1.framework.b2b.mongodb.security.jwt.cookies.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.cookies.CookieProvider;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.exceptions.cookie.CookieAccessTokenNotFoundException;
import io.tech1.framework.domain.exceptions.cookie.CookieNotFoundException;
import io.tech1.framework.domain.exceptions.cookie.CookieRefreshTokenNotFoundException;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static io.tech1.framework.domain.utilities.http.HttpCookieUtility.*;
import static io.tech1.framework.domain.utilities.numbers.LongUtility.toIntExactOrZeroOnOverflow;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CookieProviderImpl implements CookieProvider {

    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public void createJwtAccessCookie(JwtAccessToken jwtAccessToken, HttpServletResponse httpServletResponse) {
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

        httpServletResponse.addCookie(cookie);
    }

    @Override
    public void createJwtRefreshCookie(JwtRefreshToken jwtRefreshToken, HttpServletResponse httpServletResponse) {
        var securityJwtConfigs = this.applicationFrameworkProperties.getSecurityJwtConfigs();
        var refreshTokenConfiguration = securityJwtConfigs.getJwtTokensConfigs().getRefreshToken();

        var cookie = createCookie(
                refreshTokenConfiguration.getCookieKey(),
                jwtRefreshToken.value(),
                securityJwtConfigs.getCookiesConfigs().getDomain(),
                true,
                toIntExactOrZeroOnOverflow(refreshTokenConfiguration.getExpiration().getTimeAmount().toSeconds())
        );

        httpServletResponse.addCookie(cookie);
    }

    @Override
    public CookieAccessToken readJwtAccessToken(HttpServletRequest httpServletRequest) throws CookieAccessTokenNotFoundException {
        try {
            var accessToken = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getAccessToken();
            var cookie = readCookie(httpServletRequest, accessToken.getCookieKey());
            return new CookieAccessToken(cookie);
        } catch (CookieNotFoundException ex) {
            throw new CookieAccessTokenNotFoundException();
        }
    }

    @Override
    public CookieRefreshToken readJwtRefreshToken(HttpServletRequest httpServletRequest) throws CookieRefreshTokenNotFoundException {
        try {
            var refreshToken = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getRefreshToken();
            var cookie = readCookie(httpServletRequest, refreshToken.getCookieKey());
            return new CookieRefreshToken(cookie);
        } catch (CookieNotFoundException ex) {
            throw new CookieRefreshTokenNotFoundException();
        }
    }

    @Override
    public void clearCookies(HttpServletResponse httpServletResponse) {
        var securityJwtConfigs = this.applicationFrameworkProperties.getSecurityJwtConfigs();
        var cookiesConfigs = securityJwtConfigs.getCookiesConfigs();
        var accessToken = securityJwtConfigs.getJwtTokensConfigs().getAccessToken();
        var refreshToken = securityJwtConfigs.getJwtTokensConfigs().getRefreshToken();

        httpServletResponse.addCookie(createNullCookie(accessToken.getCookieKey(), cookiesConfigs.getDomain()));
        httpServletResponse.addCookie(createNullCookie(refreshToken.getCookieKey(), cookiesConfigs.getDomain()));
    }
}
