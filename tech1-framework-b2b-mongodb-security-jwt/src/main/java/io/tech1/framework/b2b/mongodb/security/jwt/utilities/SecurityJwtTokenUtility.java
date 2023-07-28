package io.tech1.framework.b2b.mongodb.security.jwt.utilities;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtTokenCreationParams;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtTokenValidatedClaims;
import io.tech1.framework.domain.properties.base.TimeAmount;

public interface SecurityJwtTokenUtility {
    JwtAccessToken createJwtAccessToken(JwtTokenCreationParams creationParams);
    JwtRefreshToken createJwtRefreshToken(JwtTokenCreationParams creationParams);
    String createJwtToken(JwtTokenCreationParams creationParams, TimeAmount timeAmount);
    JwtTokenValidatedClaims validate(JwtAccessToken jwtAccessToken);
    JwtTokenValidatedClaims validate(JwtRefreshToken jwtRefreshToken);
    boolean isExpired(JwtTokenValidatedClaims jwtTokenValidatedClaims);
}
