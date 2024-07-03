package io.tech1.framework.b2b.base.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseRefreshTokens;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;

import io.tech1.framework.foundation.domain.exceptions.tokens.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
