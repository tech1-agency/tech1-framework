package io.tech1.framework.b2b.mongodb.security.jwt.websockets.handshakes;

import io.tech1.framework.b2b.mongodb.security.jwt.services.TokenService;
import io.tech1.framework.domain.exceptions.cookie.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityHandshakeHandler extends DefaultHandshakeHandler {

    // Services
    private final TokenService tokenService;

    @Override
    protected Principal determineUser(ServerHttpRequest serverHttpRequest, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        try {
            var request = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
            var tuple2 = this.tokenService.getJwtUserByAccessTokenOrThrow(request);
            var currentJwtUser = tuple2.a();
            return new UsernamePasswordAuthenticationToken(currentJwtUser, null, currentJwtUser.getAuthorities());
        } catch (CookieAccessTokenNotFoundException | CookieRefreshTokenNotFoundException |
                 CookieAccessTokenInvalidException | CookieRefreshTokenInvalidException | CookieAccessTokenExpiredException ex) {
            throw new IllegalArgumentException("WebSocket user not determined. Exception: " + ex.getMessage());
        }
    }
}
