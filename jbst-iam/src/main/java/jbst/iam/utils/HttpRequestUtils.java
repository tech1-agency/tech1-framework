package jbst.iam.utils;

import tech1.framework.foundation.domain.http.cache.CachedBodyHttpServletRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface HttpRequestUtils {
    boolean isCachedEndpoint(HttpServletRequest request);
    void cachePayload(CachedBodyHttpServletRequest cachedRequest);
    String getCachedPayload(HttpServletRequest request);
    boolean isAuthenticationLoginEndpoint(HttpServletRequest request);
    boolean isAuthenticationRefreshTokenEndpoint(HttpServletRequest request);
}
