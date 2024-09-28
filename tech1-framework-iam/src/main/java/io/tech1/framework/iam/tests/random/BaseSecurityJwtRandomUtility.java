package io.tech1.framework.iam.tests.random;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.properties.base.TimeAmount;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static io.tech1.framework.iam.domain.jwt.JwtTokenValidatedClaims.getIssuedAt;
import static io.tech1.framework.iam.utilities.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;
import static tech1.framework.foundation.utilities.time.DateUtility.convertLocalDateTime;
import static tech1.framework.foundation.utilities.time.TimestampUtility.getCurrentTimestamp;
import static java.time.ZoneOffset.UTC;

@UtilityClass
public class BaseSecurityJwtRandomUtility {

    public static Claims validClaims() {
        var claims = Jwts.claims();
        claims.subject(Username.testsHardcoded().value());
        var timeAmount = new TimeAmount(1, ChronoUnit.HOURS);
        var expiration = convertLocalDateTime(LocalDateTime.now(UTC).plus(timeAmount.getAmount(), timeAmount.getUnit()), UTC);
        claims.issuedAt(getIssuedAt());
        claims.expiration(expiration);
        claims.add("authorities", getSimpleGrantedAuthorities("admin", "user"));
        return claims.build();
    }

    public static Claims expiredClaims() {
        var claims = Jwts.claims();
        claims.subject(Username.testsHardcoded().value());
        var currentTimestamp = getCurrentTimestamp();
        var issuedAt = new Date(currentTimestamp);
        var expiration = new Date(currentTimestamp - 1000);
        claims.issuedAt(issuedAt);
        claims.expiration(expiration);
        claims.add("authorities", getSimpleGrantedAuthorities("admin", "user"));
        return claims.build();
    }
}
