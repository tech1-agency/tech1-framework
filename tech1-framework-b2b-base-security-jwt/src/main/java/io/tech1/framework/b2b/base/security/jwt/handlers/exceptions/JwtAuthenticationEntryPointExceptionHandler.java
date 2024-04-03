package io.tech1.framework.b2b.base.security.jwt.handlers.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserLogin;
import io.tech1.framework.b2b.base.security.jwt.domain.events.EventAuthenticationLoginFailure;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.base.security.jwt.utils.HttpRequestUtils;
import io.tech1.framework.domain.exceptions.ExceptionEntity;
import io.tech1.framework.domain.http.requests.UserAgentHeader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static io.tech1.framework.domain.utilities.http.HttpServletRequestUtility.getClientIpAddr;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtAuthenticationEntryPointExceptionHandler implements AuthenticationEntryPoint {

    // Publishers
    private final SecurityJwtPublisher securityJwtPublisher;
    // Utilities
    private final HttpRequestUtils httpRequestUtils;
    // JSONs
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(this.objectMapper.writeValueAsString(ExceptionEntity.of(exception)));

        // in case of another endpoint to cache - extract methods like: isLoginEndpoint or isLogoutEndpoint
        if (exception instanceof BadCredentialsException && this.httpRequestUtils.isCachedEndpoint(request)) {
            var cachedPayload = this.httpRequestUtils.getCachedPayload(request);
            var requestUserLogin = this.objectMapper.readValue(cachedPayload, RequestUserLogin.class);

            this.securityJwtPublisher.publishAuthenticationLoginFailure(
                    new EventAuthenticationLoginFailure(
                            requestUserLogin.username(),
                            requestUserLogin.password(),
                            getClientIpAddr(request),
                            new UserAgentHeader(request)
                    )
            );
        }
    }
}
