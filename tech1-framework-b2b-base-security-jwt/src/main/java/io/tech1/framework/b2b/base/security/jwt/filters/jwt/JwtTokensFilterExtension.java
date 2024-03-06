package io.tech1.framework.b2b.base.security.jwt.filters.jwt;

import org.jetbrains.annotations.NotNull;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface JwtTokensFilterExtension {
    void doFilter(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException;
}
