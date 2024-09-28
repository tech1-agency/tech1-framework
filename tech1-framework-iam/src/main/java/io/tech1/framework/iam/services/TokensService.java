package io.tech1.framework.iam.services;

import io.tech1.framework.iam.domain.dto.responses.ResponseRefreshTokens;
import io.tech1.framework.iam.domain.jwt.JwtUser;
import io.tech1.framework.iam.domain.jwt.RequestAccessToken;
import io.tech1.framework.iam.domain.jwt.RequestRefreshToken;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech1.framework.foundation.domain.exceptions.tokens.*;

public interface TokensService {
    JwtUser getJwtUserByAccessTokenOrThrow(
            RequestAccessToken requestAccessToken,
            RequestRefreshToken requestRefreshToken
    ) throws AccessTokenInvalidException, RefreshTokenInvalidException, AccessTokenExpiredException, AccessTokenDbNotFoundException;

    ResponseRefreshTokens refreshSessionOrThrow(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws RefreshTokenNotFoundException, RefreshTokenInvalidException, RefreshTokenExpiredException, RefreshTokenDbNotFoundException;
}
