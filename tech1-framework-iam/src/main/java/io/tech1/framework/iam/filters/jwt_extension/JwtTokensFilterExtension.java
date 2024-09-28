package io.tech1.framework.iam.filters.jwt_extension;

import org.jetbrains.annotations.NotNull;

import jakarta.servlet.http.HttpServletRequest;
import tech1.framework.foundation.domain.exceptions.tokens.*;

public interface JwtTokensFilterExtension {
    void doFilter(@NotNull HttpServletRequest request) throws AccessTokenNotFoundException,
            AccessTokenExpiredException,
            RefreshTokenNotFoundException,
            AccessTokenInvalidException,
            RefreshTokenInvalidException,
            AccessTokenDbNotFoundException,
            TokenExtensionUnauthorizedException,
            TokenExtensionAccessDeniedException;
}
