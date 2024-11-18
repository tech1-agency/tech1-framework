package jbst.iam.utils;

import jbst.iam.domain.jwt.JwtAccessToken;
import jbst.iam.domain.jwt.JwtRefreshToken;
import jbst.iam.domain.jwt.JwtTokenCreationParams;
import jbst.iam.domain.jwt.JwtTokenValidatedClaims;
import tech1.framework.foundation.domain.properties.base.TimeAmount;

public interface SecurityJwtTokenUtils {
    JwtAccessToken createJwtAccessToken(JwtTokenCreationParams creationParams);
    JwtRefreshToken createJwtRefreshToken(JwtTokenCreationParams creationParams);
    String createJwtToken(JwtTokenCreationParams creationParams, TimeAmount timeAmount);
    JwtTokenValidatedClaims validate(JwtAccessToken jwtAccessToken);
    JwtTokenValidatedClaims validate(JwtRefreshToken jwtRefreshToken);
}
