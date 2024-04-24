package io.tech1.framework.b2b.base.security.jwt.filters.jwt_extension;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultJwtTokensFilterExtension implements JwtTokensFilterExtension {

    @Override
    public void doFilter(@NotNull HttpServletRequest request) {
        // no actions
    }
}
