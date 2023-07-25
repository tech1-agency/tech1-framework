package io.tech1.framework.b2b.mongodb.security.jwt.websockets.handshakes;

import io.tech1.framework.domain.exceptions.cookie.CookieNotFoundException;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

import static io.tech1.framework.domain.utilities.http.HttpCookieUtility.readCookie;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CsrfInterceptorHandshake implements HandshakeInterceptor {

    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public boolean beforeHandshake(
            @NotNull ServerHttpRequest request,
            @NotNull ServerHttpResponse response,
            @NotNull WebSocketHandler wsHandler,
            @NotNull Map<String, Object> attributes
    ) {
        try {
            var csrfConfigs = this.applicationFrameworkProperties.getSecurityJwtWebsocketsConfigs().getCsrfConfigs();
            var httpRequest = ((ServletServerHttpRequest) request).getServletRequest();
            var cookieValue = readCookie(httpRequest, csrfConfigs.getCookieName());
            var csrfToken = new DefaultCsrfToken(csrfConfigs.getHeaderName(), csrfConfigs.getParameterName(), cookieValue);
            attributes.put(CsrfToken.class.getName(), csrfToken);
            return true;
        } catch (CookieNotFoundException ex1) {
            return false;
        } catch (RuntimeException ex2) {
            LOGGER.error("Please check websocket handshake configuration. Exception: `{}`", ex2.getMessage(), ex2);
            return false;
        }
    }

    @Override
    public void afterHandshake(
            @NotNull ServerHttpRequest request,
            @NotNull ServerHttpResponse response,
            @NotNull WebSocketHandler wsHandler,
            Exception exception
    ) {
        // no actions
    }
}
