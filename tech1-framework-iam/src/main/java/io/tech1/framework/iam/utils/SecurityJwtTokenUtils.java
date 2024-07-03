package io.tech1.framework.iam.utils;

import io.tech1.framework.iam.domain.jwt.JwtAccessToken;
import io.tech1.framework.iam.domain.jwt.JwtRefreshToken;
import io.tech1.framework.iam.domain.jwt.JwtTokenCreationParams;
import io.tech1.framework.iam.domain.jwt.JwtTokenValidatedClaims;
import io.tech1.framework.foundation.domain.properties.base.TimeAmount;

public interface SecurityJwtTokenUtils {
    JwtAccessToken createJwtAccessToken(JwtTokenCreationParams creationParams);
    JwtRefreshToken createJwtRefreshToken(JwtTokenCreationParams creationParams);
    String createJwtToken(JwtTokenCreationParams creationParams, TimeAmount timeAmount);
    JwtTokenValidatedClaims validate(JwtAccessToken jwtAccessToken);
    JwtTokenValidatedClaims validate(JwtRefreshToken jwtRefreshToken);
}
