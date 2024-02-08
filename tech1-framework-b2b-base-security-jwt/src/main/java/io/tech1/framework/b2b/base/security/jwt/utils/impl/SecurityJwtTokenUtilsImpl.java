package io.tech1.framework.b2b.base.security.jwt.utils.impl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtTokenCreationParams;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtTokenValidatedClaims;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityJwtTokenUtils;
import io.tech1.framework.domain.properties.base.TimeAmount;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

import static io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtTokenValidatedClaims.getIssuedAt;
import static io.tech1.framework.domain.utilities.time.DateUtility.convertLocalDateTime;

@Slf4j
@Component
public class SecurityJwtTokenUtilsImpl implements SecurityJwtTokenUtils {

    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;
    // Values
    private final String base64EncodedSecretKey;

    @Autowired
    public SecurityJwtTokenUtilsImpl(
            ApplicationFrameworkProperties applicationFrameworkProperties
    ) {
        this.applicationFrameworkProperties = applicationFrameworkProperties;
        var jwtTokensConfigs = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs();
        jwtTokensConfigs.assertProperties("securityJwtConfigs.jwtTokensConfigs");
        this.base64EncodedSecretKey = Base64.getEncoder().encodeToString(jwtTokensConfigs.getSecretKey().getBytes());
    }

    @Override
    public JwtAccessToken createJwtAccessToken(JwtTokenCreationParams creationParams) {
        var accessTokenConfiguration = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getAccessToken();
        var jwtToken = this.createJwtToken(creationParams, accessTokenConfiguration.getExpiration());
        return new JwtAccessToken(jwtToken);
    }

    @Override
    public JwtRefreshToken createJwtRefreshToken(JwtTokenCreationParams creationParams) {
        var refreshTokenConfiguration = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getRefreshToken();
        var jwtToken = this.createJwtToken(creationParams, refreshTokenConfiguration.getExpiration());
        return new JwtRefreshToken(jwtToken);
    }

    @Override
    public String createJwtToken(JwtTokenCreationParams creationParams, TimeAmount timeAmount) {
        var claims = Jwts.claims().setSubject(creationParams.username().identifier());
        claims.put("authorities", creationParams.authorities());
        var zoneId = creationParams.zoneId();
        var expiration = LocalDateTime.now(zoneId).plus(timeAmount.getAmount(), timeAmount.getUnit());
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setClaims(claims)
                .setIssuedAt(getIssuedAt())
                .setExpiration(convertLocalDateTime(expiration, zoneId))
                .signWith(SignatureAlgorithm.HS256, this.base64EncodedSecretKey)
                .compact();
    }

    @Override
    public JwtTokenValidatedClaims validate(JwtAccessToken jwtAccessToken) {
        return this.validate(jwtAccessToken.value(), true, false);
    }

    @Override
    public JwtTokenValidatedClaims validate(JwtRefreshToken jwtRefreshToken) {
        return this.validate(jwtRefreshToken.value(), false, true);
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private JwtTokenValidatedClaims validate(String jwtToken, boolean isAccess, boolean isRefresh) {
        try {
            var claims = Jwts.parser().setSigningKey(this.base64EncodedSecretKey).parseClaimsJws(jwtToken).getBody();
            return JwtTokenValidatedClaims.valid(isAccess, isRefresh, jwtToken, claims);
        } catch (ExpiredJwtException ex1) {
            LOGGER.info("JWT token expired", ex1);
            return JwtTokenValidatedClaims.valid(isAccess, isRefresh, jwtToken, ex1.getClaims());
        } catch (JwtException | IllegalArgumentException ex2) {
            LOGGER.info("JWT token exception", ex2);
            return JwtTokenValidatedClaims.invalid(isAccess, isRefresh, jwtToken);
        }
    }
}
