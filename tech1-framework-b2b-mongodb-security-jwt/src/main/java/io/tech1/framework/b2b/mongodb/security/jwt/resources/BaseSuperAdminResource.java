package io.tech1.framework.b2b.mongodb.security.jwt.resources;

import io.tech1.framework.b2b.mongodb.security.jwt.annotations.AbstractFrameworkBaseSecurityResource;
import io.tech1.framework.b2b.mongodb.security.jwt.cookies.CookieProvider;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseInvitationCode1;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseServerSessionsTable;
import io.tech1.framework.b2b.mongodb.security.jwt.services.BaseSuperAdminService;
import io.tech1.framework.b2b.mongodb.security.jwt.services.UserSessionService;
import io.tech1.framework.domain.exceptions.cookie.CookieRefreshTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@AbstractFrameworkBaseSecurityResource
@RestController
@RequestMapping("/superadmin")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseSuperAdminResource {

    // Services
    private final BaseSuperAdminService baseSuperAdminService;
    private final UserSessionService userSessionService;
    // Cookie
    private final CookieProvider cookieProvider;

    @GetMapping("/invitationCodes/unused")
    public List<ResponseInvitationCode1> getUnusedInvitationCodes() {
        return this.baseSuperAdminService.findUnused();
    }

    @GetMapping("/sessions")
    public ResponseServerSessionsTable getServerSessions(HttpServletRequest httpServletRequest) throws CookieRefreshTokenNotFoundException {
        var cookie = this.cookieProvider.readJwtRefreshToken(httpServletRequest);
        return this.baseSuperAdminService.getServerSessions(cookie);
    }

    @DeleteMapping("/sessions/{sessionId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable String sessionId) {
        this.userSessionService.deleteById(sessionId);
    }

    @DeleteMapping("/sessions")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAllExceptCurrent(HttpServletRequest httpServletRequest) throws CookieRefreshTokenNotFoundException {
        var cookie = this.cookieProvider.readJwtRefreshToken(httpServletRequest);
        this.userSessionService.deleteAllExceptCurrentAsSuperuser(cookie);
    }
}

