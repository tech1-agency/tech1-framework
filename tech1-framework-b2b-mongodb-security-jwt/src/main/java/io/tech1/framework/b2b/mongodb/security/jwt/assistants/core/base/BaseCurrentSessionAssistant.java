package io.tech1.framework.b2b.mongodb.security.jwt.assistants.core.base;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.b2b.mongodb.security.jwt.assistants.core.CurrentSessionAssistant;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseUserSessionsTable;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.security.CurrentClientUser;
import io.tech1.framework.b2b.mongodb.security.jwt.services.UserSessionService;
import io.tech1.framework.b2b.mongodb.security.jwt.utilities.SecurityPrincipalUtility;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.hardware.monitoring.store.HardwareMonitoringStore;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseCurrentSessionAssistant implements CurrentSessionAssistant {
    private static final String HARDWARE = "hardware";

    // Sessions
    protected final SessionRegistry sessionRegistry;
    // Service
    protected final UserSessionService userSessionService;
    // Stores
    protected final HardwareMonitoringStore hardwareMonitoringStore;
    // Utilities
    protected final SecurityPrincipalUtility securityPrincipalUtility;
    // Properties
    protected final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public Username getCurrentUsername() {
        return Username.of(this.securityPrincipalUtility.getAuthenticatedUsername());
    }

    @Override
    public JwtUser getCurrentJwtUser() {
        return this.securityPrincipalUtility.getAuthenticatedJwtUser();
    }

    @Override
    public CurrentClientUser getCurrentClientUser() {
        var user = this.getCurrentJwtUser();

        var attributes = nonNull(user.attributes()) ? user.attributes() : new HashMap<String, Object>();
        if (this.applicationFrameworkProperties.getHardwareMonitoringConfigs().isEnabled()) {
            attributes.put(HARDWARE, this.hardwareMonitoringStore.getHardwareMonitoringWidget());
        }

        return new CurrentClientUser(
                user.username(),
                user.email(),
                user.name(),
                user.zoneId(),
                user.authorities(),
                attributes
        );
    }

    @Override
    public ResponseUserSessionsTable getCurrentUserDbSessionsTable(CookieRefreshToken cookie) {
        var username = this.getCurrentUsername();
        this.sessionRegistry.cleanByExpiredRefreshTokens(Set.of(username));
        var activeUsersSessions = this.userSessionService.findByUsername(username);
        var sessions = activeUsersSessions.stream()
                .map(session -> ResponseUserSession2.of(session, cookie))
                .collect(Collectors.toList());
        return ResponseUserSessionsTable.of(sessions);
    }
}
