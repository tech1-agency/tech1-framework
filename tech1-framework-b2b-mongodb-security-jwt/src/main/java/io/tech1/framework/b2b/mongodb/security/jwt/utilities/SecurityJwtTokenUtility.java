package io.tech1.framework.b2b.mongodb.security.jwt.utilities;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtTokenValidatedClaims;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.properties.base.TimeAmount;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public interface SecurityJwtTokenUtility {
    JwtAccessToken createJwtAccessToken(DbUser dbUser);
    JwtRefreshToken createJwtRefreshToken(DbUser dbUser);
    String createJwtToken(Username username, List<SimpleGrantedAuthority> authorities, TimeAmount timeAmount);
    JwtTokenValidatedClaims validate(JwtAccessToken jwtAccessToken);
    JwtTokenValidatedClaims validate(JwtRefreshToken jwtRefreshToken);
    boolean isExpired(JwtTokenValidatedClaims jwtTokenValidatedClaims);
}
