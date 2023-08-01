package io.tech1.framework.b2b.base.security.jwt.domain.jwt;

import io.jsonwebtoken.Claims;
import io.tech1.framework.domain.base.Username;

import java.util.Date;

public record JwtTokenValidatedClaims(
        boolean valid,
        boolean isAccess,
        boolean isRefresh,
        String jwtToken,
        @Deprecated Claims claims,
        Username username,
        Date expirationDate
) {
    private static final Username INVALID = Username.of("invalid");

    public static Date getIssuedAt() {
        return new Date();
    }

    public static JwtTokenValidatedClaims invalid(boolean isAccess, boolean isRefresh, String jwtToken) {
        return new JwtTokenValidatedClaims(false, isAccess, isRefresh, jwtToken, null, INVALID, new Date(0));
    }

    public static JwtTokenValidatedClaims invalid(JwtAccessToken accessToken) {
        return invalid(true, false, accessToken.value());
    }

    public static JwtTokenValidatedClaims invalid(JwtRefreshToken refreshToken) {
        return invalid(false, true, refreshToken.value());
    }

    public static JwtTokenValidatedClaims valid(boolean isAccess, boolean isRefresh, String jwtToken, Claims claims) {
        var username = Username.of(claims.getSubject());
        var expirationDate = claims.getExpiration();
        return new JwtTokenValidatedClaims(true, isAccess, isRefresh, jwtToken, claims, username, expirationDate);
    }

    public static JwtTokenValidatedClaims valid(JwtAccessToken accessToken, Claims claims) {
        return valid(true, false, accessToken.value(), claims);
    }

    public static JwtTokenValidatedClaims valid(JwtRefreshToken refreshToken, Claims claims) {
        return valid(false, true, refreshToken.value(), claims);
    }

    public boolean isInvalid() {
        return !this.valid;
    }

    public boolean isExpired() {
        return this.isInvalid() || getIssuedAt().after(this.expirationDate);
    }

    public long getExpirationTimestamp() {
        return this.expirationDate.getTime();
    }
}
