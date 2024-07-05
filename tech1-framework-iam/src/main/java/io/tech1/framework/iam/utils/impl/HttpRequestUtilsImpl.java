package io.tech1.framework.iam.utils.impl;

import io.tech1.framework.foundation.domain.http.cache.CachedBodyHttpServletRequest;
import io.tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import io.tech1.framework.iam.utils.HttpRequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static io.tech1.framework.foundation.utilities.http.HttpServletRequestUtility.isPOST;

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
    public void cachePayload(CachedBodyHttpServletRequest cachedRequest) {
        if (this.isCachedEndpoint(cachedRequest)) {
            cachedRequest.setAttribute(CACHED_PAYLOAD_ATTRIBUTE, cachedRequest.getCachedPayload().value());
        }
    }

    @Override
    public String getCachedPayload(HttpServletRequest request) {
        return (String) request.getAttribute(CACHED_PAYLOAD_ATTRIBUTE);
    }

    @Override
    public boolean isAuthenticationLoginEndpoint(HttpServletRequest request) {
        return isPOST(request) && this.isEndpoint(request, "/authentication/login");
    }

    @Override
    public boolean isAuthenticationRefreshTokenEndpoint(HttpServletRequest request) {
        return isPOST(request) && this.isEndpoint(request, "/authentication/refreshToken");
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private boolean isEndpoint(HttpServletRequest request, String requestMapping) {
        var frameworkBasePathPrefix = this.applicationFrameworkProperties.getMvcConfigs().getFrameworkBasePathPrefix();
        var requestURI = this.contextPath + frameworkBasePathPrefix + requestMapping;
        return requestURI.equals(request.getRequestURI());
    }
}
