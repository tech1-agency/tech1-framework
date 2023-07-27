package io.tech1.framework.b2b.mongodb.security.jwt.utilities.impl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtTokenValidatedClaims;
import io.tech1.framework.b2b.mongodb.security.jwt.utilities.SecurityJwtTokenUtility;
import io.tech1.framework.domain.properties.base.TimeAmount;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import static io.tech1.framework.domain.properties.utilities.PropertiesAsserter.assertProperties;
import static io.tech1.framework.domain.utilities.time.DateUtility.convertLocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityJwtTokenUtilityImpl implements SecurityJwtTokenUtility {

    // TODO [YY] @PostConstruct -> constructor
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;
    // Values
    private String base64EncodedSecretKey;

    @PostConstruct
    public void postConstruct() {
        assertProperties(
                this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs(),
                "securityJwtConfigs.jwtTokensConfigs"
        );
        var secretKey = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getSecretKey();
        this.base64EncodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    @Override
    public JwtAccessToken createJwtAccessToken(DbUser user) {
        var accessTokenConfiguration = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getAccessToken();
        var jwtToken = this.createJwtToken(user, accessTokenConfiguration.getExpiration());
        return new JwtAccessToken(jwtToken);
    }

    @Override
    public JwtRefreshToken createJwtRefreshToken(DbUser user) {
        var refreshTokenConfiguration = this.applicationFrameworkProperties.getSecurityJwtConfigs().getJwtTokensConfigs().getRefreshToken();
        var jwtToken = this.createJwtToken(user, refreshTokenConfiguration.getExpiration());
        return new JwtRefreshToken(jwtToken);
    }

    @Override
    public String createJwtToken(DbUser user, TimeAmount timeAmount) {
        var claims = Jwts.claims().setSubject(user.getUsername().identifier());
        claims.put("authorities", user.getAuthorities());
        var zoneId = user.getZoneId();
        var expiration = LocalDateTime.now(zoneId).plus(timeAmount.getAmount(), timeAmount.getUnit());
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setClaims(claims)
                .setIssuedAt(this.getIssuedAt())
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

    @Override
    public boolean isExpired(JwtTokenValidatedClaims jwtTokenValidatedClaims) {
        return jwtTokenValidatedClaims.valid() && this.getIssuedAt().after(jwtTokenValidatedClaims.claims().getExpiration());
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private Date getIssuedAt() {
        return new Date();
    }

    private JwtTokenValidatedClaims validate(String jwtToken, boolean isAccess, boolean isRefresh) {
        try {
            var claims = Jwts.parser().setSigningKey(this.base64EncodedSecretKey).parseClaimsJws(jwtToken);
            return JwtTokenValidatedClaims.valid(isAccess, isRefresh, jwtToken, claims.getBody());
        } catch (ExpiredJwtException ex1) {
            LOGGER.error("JWT token expired", ex1);
            return JwtTokenValidatedClaims.valid(isAccess, isRefresh, jwtToken, ex1.getClaims());
        } catch (JwtException | IllegalArgumentException ex2) {
            LOGGER.error("JWT token exception", ex2);
            return JwtTokenValidatedClaims.invalid(isAccess, isRefresh, jwtToken);
        }
    }
}
