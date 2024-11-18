package jbst.iam.filters.jwt_extension;

import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
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
