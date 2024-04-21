package io.tech1.framework.b2b.base.security.jwt.filters.jwt_extension;

import io.tech1.framework.domain.exceptions.tokens.*;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;

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
