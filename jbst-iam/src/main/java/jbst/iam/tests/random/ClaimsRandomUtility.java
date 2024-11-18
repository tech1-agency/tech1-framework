package jbst.iam.tests.random;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.experimental.UtilityClass;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.properties.base.TimeAmount;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static java.time.ZoneOffset.UTC;
import static jbst.iam.domain.jwt.JwtTokenValidatedClaims.getIssuedAt;
import static jbst.iam.utilities.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;
import static tech1.framework.foundation.utilities.time.DateUtility.convertLocalDateTime;
import static tech1.framework.foundation.utilities.time.TimestampUtility.getCurrentTimestamp;

@UtilityClass
public class ClaimsRandomUtility {

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
