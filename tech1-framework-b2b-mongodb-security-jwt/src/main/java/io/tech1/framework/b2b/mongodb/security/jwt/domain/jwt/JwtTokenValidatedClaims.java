package io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt;

import io.jsonwebtoken.Claims;
import io.tech1.framework.domain.base.Username;

import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static java.util.Objects.nonNull;

public record JwtTokenValidatedClaims(
        boolean valid,
        boolean isAccess,
        boolean isRefresh,
        String jwtToken,
        Claims claims
) {
    public static JwtTokenValidatedClaims invalid(boolean isAccess, boolean isRefresh, String jwtToken) {
        return new JwtTokenValidatedClaims(false, isAccess, isRefresh, jwtToken, null);
    }

    public static JwtTokenValidatedClaims invalid(JwtAccessToken jwtAccessToken) {
        return new JwtTokenValidatedClaims(false, true, false, jwtAccessToken.value(), null);
    }

    public static JwtTokenValidatedClaims invalid(JwtRefreshToken jwtRefreshToken) {
        return new JwtTokenValidatedClaims(false, false, true, jwtRefreshToken.value(), null);
    }

    public static JwtTokenValidatedClaims valid(boolean isAccess, boolean isRefresh, String jwtToken, Claims claims) {
        return new JwtTokenValidatedClaims(true, isAccess, isRefresh, jwtToken, claims);
    }

    public static JwtTokenValidatedClaims valid(JwtAccessToken jwtAccessToken, Claims claims) {
        return new JwtTokenValidatedClaims(true, true, false, jwtAccessToken.value(), claims);
    }

    public static JwtTokenValidatedClaims valid(JwtRefreshToken jwtRefreshToken, Claims claims) {
        return new JwtTokenValidatedClaims(true, false, true, jwtRefreshToken.value(), claims);
    }

    public boolean isInvalid() {
        return !this.valid;
    }

    public Username safeGetUsername() {
        if (this.valid && nonNull(this.claims)) {
            return Username.of(this.claims.getSubject());
        } else {
            throw new IllegalArgumentException(invalidAttribute("JwtTokenValidatedClaims"));
        }
    }

    public long safeGetExpirationTimestamp() {
        if (this.valid && nonNull(this.claims)) {
            return this.claims.getExpiration().getTime();
        } else {
            throw new IllegalArgumentException(invalidAttribute("JwtTokenValidatedClaims"));
        }
    }
}
