package io.tech1.framework.b2b.base.security.jwt.domain.jwt;

import io.jsonwebtoken.Claims;
import io.tech1.framework.domain.base.Username;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

public record JwtTokenValidatedClaims(
        boolean valid,
        boolean isAccess,
        boolean isRefresh,
        String jwtToken,
        Username username,
        Date expirationDate,
        List<SimpleGrantedAuthority> authorities
) {
    private static final Username INVALID = Username.of("invalid");

    public static Date getIssuedAt() {
        return new Date();
    }

    public static JwtTokenValidatedClaims invalid(boolean isAccess, boolean isRefresh, String jwtToken) {
        return new JwtTokenValidatedClaims(false, isAccess, isRefresh, jwtToken, INVALID, new Date(0), new ArrayList<>());
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
        var authorities = Arrays.stream(
                claims.get("authorities").toString()
                        .replace("[", "")
                        .replace("]", "")
                        .replace("{", "")
                        .replace("}", "")
                        .replace("authority=", "")
                        .split(",")
                )
                .map(rawUserRole -> new SimpleGrantedAuthority(rawUserRole.trim()))
                .toList();
        return new JwtTokenValidatedClaims(true, isAccess, isRefresh, jwtToken, username, expirationDate, authorities);
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

    public Set<String> authoritiesAsStrings() {
        return this.authorities.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toSet());
    }
}
