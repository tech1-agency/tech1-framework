package io.tech1.framework.b2b.mongodb.security.jwt.resources;

import io.tech1.framework.b2b.mongodb.security.jwt.annotations.AbstractFrameworkBaseSecurityResource;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseInvitationCode1;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseServerSessionsTable;
import io.tech1.framework.b2b.mongodb.security.jwt.services.BaseSuperAdminService;
import io.tech1.framework.b2b.mongodb.security.jwt.services.UserSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/invitationCodes/unused")
    public List<ResponseInvitationCode1> getUnusedInvitationCodes() {
        return this.baseSuperAdminService.findUnused();
    }

    @GetMapping("/sessions")
    public ResponseServerSessionsTable getServerSessions() {
        return this.baseSuperAdminService.getServerSessions();
    }

    @DeleteMapping("/sessions/{sessionId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable String sessionId) {
        this.userSessionService.deleteById(sessionId);
    }
}

