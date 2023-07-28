package io.tech1.framework.b2b.mongodb.security.jwt.resources;

import io.tech1.framework.b2b.base.security.jwt.annotations.AbstractFrameworkBaseSecurityResource;
import io.tech1.framework.b2b.mongodb.security.jwt.assistants.core.CurrentSessionAssistant;
import io.tech1.framework.b2b.mongodb.security.jwt.cookies.CookieProvider;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseUserSessionsTable;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.security.CurrentClientUser;
import io.tech1.framework.b2b.mongodb.security.jwt.services.UserSessionService;
import io.tech1.framework.b2b.base.security.jwt.validators.SessionsRequestsValidator;
import io.tech1.framework.domain.exceptions.cookie.CookieRefreshTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@AbstractFrameworkBaseSecurityResource
@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseSecuritySessionsResource {

    // Assistants
    private final CurrentSessionAssistant currentSessionAssistant;
    // Services
    private final UserSessionService userSessionService;
    // Cookie
    private final CookieProvider cookieProvider;
    // Validators
    private final SessionsRequestsValidator sessionsRequestsValidator;

    @GetMapping
    public ResponseUserSessionsTable getCurrentUserDbSessions(HttpServletRequest httpServletRequest) throws CookieRefreshTokenNotFoundException {
        var cookie = this.cookieProvider.readJwtRefreshToken(httpServletRequest);
        return this.currentSessionAssistant.getCurrentUserDbSessionsTable(cookie);
    }

    @GetMapping("/current")
    public CurrentClientUser getCurrentClientUser() {
        return this.currentSessionAssistant.getCurrentClientUser();
    }

    // WARNING: should NOT be used, under development
    @DeleteMapping("/{sessionId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable String sessionId) {
        var username = this.currentSessionAssistant.getCurrentUsername();
        this.sessionsRequestsValidator.validateDeleteById(username, sessionId);
        this.userSessionService.deleteById(sessionId);
    }

    // WARNING: should NOT be used, under development
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllExceptCurrent(HttpServletRequest httpServletRequest) throws CookieRefreshTokenNotFoundException {
        var currentDbUser = this.currentSessionAssistant.getCurrentDbUser();
        var cookie = this.cookieProvider.readJwtRefreshToken(httpServletRequest);
        this.userSessionService.deleteAllExceptCurrent(currentDbUser, cookie);
    }
}

