package io.tech1.framework.b2b.mongodb.security.jwt.assistants.core.base;

import io.tech1.framework.b2b.mongodb.security.jwt.assistants.core.CurrentSessionAssistant;
import io.tech1.framework.b2b.mongodb.security.jwt.cookies.CookieProvider;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseUserSessionsTable;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.security.CurrentClientUser;
import io.tech1.framework.b2b.mongodb.security.jwt.services.UserSessionService;
import io.tech1.framework.b2b.mongodb.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.b2b.mongodb.security.jwt.utilities.SecurityPrincipalUtility;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.exceptions.cookie.CookieRefreshTokenNotFoundException;
import io.tech1.framework.hardware.monitoring.store.HardwareMonitoringStore;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

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
    // Cookie
    protected final CookieProvider cookieProvider;
    // Utilities
    protected final SecurityPrincipalUtility securityPrincipalUtility;
    // Properties
    protected final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public Username getCurrentUsername() {
        return Username.of(this.securityPrincipalUtility.getAuthenticatedUsername());
    }

    @Override
    public String getCurrentUserId() {
        return this.getCurrentJwtUser().getDbUser().getId();
    }

    @Override
    public DbUser getCurrentDbUser() {
        return this.getCurrentJwtUser().getDbUser();
    }

    @Override
    public JwtUser getCurrentJwtUser() {
        return this.securityPrincipalUtility.getAuthenticatedJwtUser();
    }

    @Override
    public CurrentClientUser getCurrentClientUser() {
        var currentJwtUser = this.getCurrentJwtUser();

        var user = currentJwtUser.getDbUser();

        var attributes = user.getNotNullAttributes();
        if (this.applicationFrameworkProperties.getHardwareMonitoringConfigs().isEnabled()) {
            attributes.put(HARDWARE, this.hardwareMonitoringStore.getHardwareMonitoringWidget());
        }

        return new CurrentClientUser(
                user.getUsername(),
                user.getEmail(),
                user.getName(),
                user.getZoneId(),
                user.getAuthorities(),
                attributes
        );
    }

    @Override
    public ResponseUserSessionsTable getCurrentUserDbSessionsTable(HttpServletRequest httpServletRequest) throws CookieRefreshTokenNotFoundException {
        var currentJwtUser = this.getCurrentJwtUser();
        var username = currentJwtUser.getDbUser().getUsername();
        var usersSessions = this.userSessionService.findByUsername(username);
        this.sessionRegistry.cleanByExpiredRefreshTokens(usersSessions);
        var activeUsersSessions = this.userSessionService.findByUsername(username);
        var cookieRefreshToken = this.cookieProvider.readJwtRefreshToken(httpServletRequest);
        var sessions = activeUsersSessions.stream()
                .map(session -> new ResponseUserSession2(session, cookieRefreshToken))
                .collect(Collectors.toList());
        return new ResponseUserSessionsTable(sessions);
    }
}
