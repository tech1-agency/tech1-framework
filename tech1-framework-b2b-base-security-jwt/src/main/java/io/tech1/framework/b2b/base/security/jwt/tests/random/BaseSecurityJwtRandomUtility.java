package io.tech1.framework.b2b.base.security.jwt.tests.random;

import io.jsonwebtoken.impl.DefaultClaims;
import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserRegistration1;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.properties.base.TimeAmount;
import io.tech1.framework.domain.utilities.time.TimestampUtility;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtTokenValidatedClaims.getIssuedAt;
import static io.tech1.framework.domain.base.AbstractAuthority.SUPER_ADMIN;
import static io.tech1.framework.domain.tests.constants.TestsUsernamesConstants.TECH1;
import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static io.tech1.framework.domain.utilities.time.DateUtility.convertLocalDateTime;
import static java.time.ZoneOffset.UTC;

@UtilityClass
public class BaseSecurityJwtRandomUtility {

    public static JwtUser randomJwtUser() {
        return new JwtUser(
                entity(UserId.class),
                randomUsername(),
                randomPassword(),
                randomZoneId(),
                authorities(SUPER_ADMIN),
                randomEmail(),
                randomString(),
                new HashMap<>()
        );
    }

    public static List<SimpleGrantedAuthority> authorities(String... authorities) {
        return Stream.of(authorities).map(SimpleGrantedAuthority::new).toList();
    }

    public static DefaultClaims validDefaultClaims() {
        var claims = new DefaultClaims();
        claims.setSubject(TECH1.identifier());
        var timeAmount = TimeAmount.of(1, ChronoUnit.HOURS);
        var expiration = convertLocalDateTime(LocalDateTime.now(UTC).plus(timeAmount.getAmount(), timeAmount.getUnit()), UTC);
        claims.setIssuedAt(getIssuedAt());
        claims.setExpiration(expiration);
        claims.put("authorities", authorities("admin", "user"));
        return claims;
    }

    public static DefaultClaims expiredDefaultClaims() {
        var claims = new DefaultClaims();
        claims.setSubject(TECH1.identifier());
        var currentTimestamp = TimestampUtility.getCurrentTimestamp();
        var issuedAt = new Date(currentTimestamp);
        var expiration = new Date(currentTimestamp - 1000);
        claims.setIssuedAt(issuedAt);
        claims.setExpiration(expiration);
        claims.put("authorities", authorities("admin", "user"));
        return claims;
    }

    public static AnyDbInvitationCode validAnyDbInvitationCode() {
        return new AnyDbInvitationCode(entity(InvitationCodeId.class), randomUsername(), authorities(SUPER_ADMIN), randomString(), randomUsername());
    }

    public static RequestUserRegistration1 validRegistration1() {
        return new RequestUserRegistration1(Username.of("registration11"), randomPassword(), randomPassword(), randomZoneId().getId(), randomString());
    }
}
