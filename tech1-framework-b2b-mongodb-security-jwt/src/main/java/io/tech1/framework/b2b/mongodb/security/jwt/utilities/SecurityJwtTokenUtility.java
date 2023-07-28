package io.tech1.framework.b2b.mongodb.security.jwt.utilities;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtTokenValidatedClaims;
import io.tech1.framework.domain.properties.base.TimeAmount;

public interface SecurityJwtTokenUtility {
    JwtAccessToken createJwtAccessToken(DbUser user);
    JwtRefreshToken createJwtRefreshToken(DbUser user);
    String createJwtToken(DbUser user, TimeAmount timeAmount);
    JwtTokenValidatedClaims validate(JwtAccessToken jwtAccessToken);
    JwtTokenValidatedClaims validate(JwtRefreshToken jwtRefreshToken);
    boolean isExpired(JwtTokenValidatedClaims jwtTokenValidatedClaims);
}
