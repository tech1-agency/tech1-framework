package io.tech1.framework.iam.filters.logging;

import io.tech1.framework.iam.utils.HttpRequestUtils;
import io.tech1.framework.iam.utils.SecurityPrincipalUtils;
import io.tech1.framework.foundation.domain.http.cache.CachedBodyHttpServletRequest;
import io.tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import io.tech1.framework.iam.utilities.HttpServletRequestUtility;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdvancedRequestLoggingFilter extends OncePerRequestFilter {

    // Utilities
    private final HttpRequestUtils httpRequestUtils;
    private final SecurityPrincipalUtils securityPrincipalUtils;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (HttpServletRequestUtility.isMultipartRequest(request)) {
            filterChain.doFilter(request, response);
        } else {
            var cachedRequest = new CachedBodyHttpServletRequest(request);
            this.httpRequestUtils.cachePayload(cachedRequest);

            if (this.applicationFrameworkProperties.getSecurityJwtConfigs().getLoggingConfigs().isAdvancedRequestLoggingEnabled()) {
                LOGGER.info("============================================================================================");
                LOGGER.info("Method: (@" + cachedRequest.getMethod() + ", " + cachedRequest.getServletPath() + ")");
                LOGGER.info("Current User: " + this.securityPrincipalUtils.getAuthenticatedUsernameOrUnexpected());
                var payload = cachedRequest.getCachedPayload();
                if (!payload.value().isBlank()) {
                    LOGGER.info("Payload: \n" + payload);
                } else {
                    LOGGER.info("No Payload");
                }
                LOGGER.info("============================================================================================");
            }

            filterChain.doFilter(cachedRequest, response);
        }
    }
}
