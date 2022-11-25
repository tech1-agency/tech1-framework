package io.tech1.framework.b2b.mongodb.security.jwt.resources;

import io.tech1.framework.b2b.mongodb.security.jwt.annotations.AbstractFrameworkBaseSecurityResource;
import io.tech1.framework.b2b.mongodb.security.jwt.assistants.core.CurrentSessionAssistant;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseUserSessionsTable;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.security.CurrentClientUser;
import io.tech1.framework.domain.exceptions.cookie.CookieRefreshTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@AbstractFrameworkBaseSecurityResource
@RestController
@RequestMapping("/session")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseSecuritySessionResource {

    // Assistants
    private final CurrentSessionAssistant currentSessionAssistant;

    @GetMapping("/current")
    public CurrentClientUser getCurrentClientUser() {
        return this.currentSessionAssistant.getCurrentClientUser();
    }

    @GetMapping("/db/table")
    public ResponseUserSessionsTable getCurrentUserDbSessions(HttpServletRequest httpServletRequest) throws CookieRefreshTokenNotFoundException {
        return this.currentSessionAssistant.getCurrentUserDbSessionsTable(httpServletRequest);
    }
}

