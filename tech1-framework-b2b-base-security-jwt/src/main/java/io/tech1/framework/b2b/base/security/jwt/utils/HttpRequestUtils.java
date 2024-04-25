package io.tech1.framework.b2b.base.security.jwt.utils;

import io.tech1.framework.domain.http.cache.CachedBodyHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface HttpRequestUtils {
    boolean isCachedEndpoint(HttpServletRequest request);
    void cachePayload(CachedBodyHttpServletRequest cachedRequest, String payload);
    String getCachedPayload(HttpServletRequest request);
    boolean isAuthenticationLoginEndpoint(HttpServletRequest request);
    boolean isAuthenticationRefreshTokenEndpoint(HttpServletRequest request);
}
