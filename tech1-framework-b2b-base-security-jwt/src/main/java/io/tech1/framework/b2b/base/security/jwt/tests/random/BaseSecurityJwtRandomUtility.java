package io.tech1.framework.b2b.base.security.jwt.tests.random;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import io.tech1.framework.b2b.base.security.jwt.domain.db.InvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.db.UserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserRegistration1;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserId;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.properties.base.TimeAmount;
import io.tech1.framework.domain.system.reset_server.ResetServerStatus;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtTokenValidatedClaims.getIssuedAt;
import static io.tech1.framework.b2b.base.security.jwt.utilities.SpringAuthoritiesUtility.getSimpleGrantedAuthorities;
import static io.tech1.framework.domain.base.AbstractAuthority.SUPER_ADMIN;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static io.tech1.framework.domain.utilities.time.DateUtility.convertLocalDateTime;
import static io.tech1.framework.domain.utilities.time.TimestampUtility.getCurrentTimestamp;
import static java.time.ZoneOffset.UTC;

@UtilityClass
public class BaseSecurityJwtRandomUtility {

    public static JwtUser randomSuperadmin() {
        return new JwtUser(
                UserId.random(),
                Username.random(),
                Password.random(),
                randomZoneId(),
                getSimpleGrantedAuthorities(SUPER_ADMIN),
                Email.random(),
                randomString(),
                new HashMap<>()
        );
    }

    public static Set<JwtAccessToken> accessTokens(String... accessTokens) {
        return Stream.of(accessTokens).map(JwtAccessToken::new).collect(Collectors.toSet());
    }

    public static Claims validClaims() {
        var claims = new DefaultClaims();
        claims.setSubject(Username.testsHardcoded().value());
        var timeAmount = new TimeAmount(1, ChronoUnit.HOURS);
        var expiration = convertLocalDateTime(LocalDateTime.now(UTC).plus(timeAmount.getAmount(), timeAmount.getUnit()), UTC);
        claims.setIssuedAt(getIssuedAt());
        claims.setExpiration(expiration);
        claims.put("authorities", getSimpleGrantedAuthorities("admin", "user"));
        return claims;
    }

    public static Claims expiredClaims() {
        var claims = new DefaultClaims();
        claims.setSubject(Username.testsHardcoded().value());
        var currentTimestamp = getCurrentTimestamp();
        var issuedAt = new Date(currentTimestamp);
        var expiration = new Date(currentTimestamp - 1000);
        claims.setIssuedAt(issuedAt);
        claims.setExpiration(expiration);
        claims.put("authorities", getSimpleGrantedAuthorities("admin", "user"));
        return claims;
    }

    public static InvitationCode randomInvitationCode() {
        return new InvitationCode(InvitationCodeId.random(), Username.random(), getSimpleGrantedAuthorities(SUPER_ADMIN), randomString(), Username.random());
    }

    public static UserSession randomPersistedSession() {
        return UserSession.ofPersisted(
                UserSessionId.random(),
                getCurrentTimestamp(),
                getCurrentTimestamp(),
                Username.random(),
                JwtAccessToken.random(),
                JwtRefreshToken.random(),
                UserRequestMetadata.random(),
                randomBoolean(),
                randomBoolean()
        );
    }

    public static RequestUserRegistration1 registration1() {
        return new RequestUserRegistration1(Username.of("registration11"), Password.random(), Password.random(), randomZoneId().getId(), randomString());
    }

    public static ResetServerStatus randomResetServerStatus() {
        return new ResetServerStatus(10);
    }
}
