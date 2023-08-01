package io.tech1.framework.b2b.base.security.jwt.tests.random;

import io.jsonwebtoken.impl.DefaultClaims;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Stream;

import static io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtTokenValidatedClaims.getIssuedAt;
import static io.tech1.framework.domain.tests.constants.TestsUsernamesConstants.TECH1;

@UtilityClass
public class BaseSecurityJwtRandomUtility {

    public static List<SimpleGrantedAuthority> authorities(String... authorities) {
        return Stream.of(authorities).map(SimpleGrantedAuthority::new).toList();
    }

    public static DefaultClaims validDefaultClaims() {
        var claims = new DefaultClaims();
        claims.setSubject(TECH1.identifier());
        claims.setIssuedAt(getIssuedAt());
        claims.setExpiration(getIssuedAt());
        claims.put("authorities", authorities("admin", "user"));
        return claims;
    }
}
