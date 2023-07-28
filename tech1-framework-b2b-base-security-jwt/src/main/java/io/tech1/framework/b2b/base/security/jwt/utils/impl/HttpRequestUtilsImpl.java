package io.tech1.framework.b2b.base.security.jwt.utils.impl;

import io.tech1.framework.b2b.base.security.jwt.utils.HttpRequestUtils;
import io.tech1.framework.domain.http.cache.CachedBodyHttpServletRequest;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HttpRequestUtilsImpl implements HttpRequestUtils {
    protected static final String CACHED_PAYLOAD_ATTRIBUTE = "tech1-security-jwt-cached-payload-attribute";

    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    public boolean isCachedEndpoint(HttpServletRequest request) {
        return this.isAuthenticationLoginEndpoint(request);
    }

    @Override
    public void cachePayload(CachedBodyHttpServletRequest cachedRequest, String payload) {
        cachedRequest.setAttribute(CACHED_PAYLOAD_ATTRIBUTE, payload);
    }

    @Override
    public String getCachedPayload(HttpServletRequest request) {
        return (String) request.getAttribute(CACHED_PAYLOAD_ATTRIBUTE);
    }

    @Override
    public boolean isAuthenticationLoginEndpoint(HttpServletRequest request) {
        return this.isEndpoint(request, "POST", "/authentication/login");
    }

    @Override
    public boolean isAuthenticationRefreshTokenEndpoint(HttpServletRequest request) {
        return this.isEndpoint(request, "POST", "/authentication/refreshToken");
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private boolean isEndpoint(HttpServletRequest request, String requestMethod, String requestMapping) {
        var frameworkBasePathPrefix = this.applicationFrameworkProperties.getMvcConfigs().getFrameworkBasePathPrefix();
        var requestURI = this.contextPath + frameworkBasePathPrefix + requestMapping;
        return requestMethod.equals(request.getMethod()) && requestURI.equals(request.getRequestURI());
    }
}
