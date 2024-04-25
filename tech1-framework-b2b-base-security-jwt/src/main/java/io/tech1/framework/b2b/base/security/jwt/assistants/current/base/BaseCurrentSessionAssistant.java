package io.tech1.framework.b2b.base.security.jwt.assistants.current.base;

import io.tech1.framework.b2b.base.security.jwt.assistants.current.CurrentSessionAssistant;
import io.tech1.framework.b2b.base.security.jwt.domain.db.UserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSessionsTable;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.security.CurrentClientUser;
import io.tech1.framework.b2b.base.security.jwt.repositories.UsersSessionsRepository;
import io.tech1.framework.b2b.base.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.b2b.base.security.jwt.tokens.facade.TokensProvider;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityPrincipalUtils;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.exceptions.tokens.AccessTokenNotFoundException;
import io.tech1.framework.hardware.monitoring.store.HardwareMonitoringStore;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Set;

import static java.util.Objects.nonNull;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseCurrentSessionAssistant implements CurrentSessionAssistant {
    private static final String HARDWARE = "hardware";

    // Sessions
    protected final SessionRegistry sessionRegistry;
    // Repositories
    protected final UsersSessionsRepository usersSessionsRepository;
    // Stores
    protected final HardwareMonitoringStore hardwareMonitoringStore;
    // Tokens
    protected final TokensProvider tokensProvider;
    // Utilities
    protected final SecurityPrincipalUtils securityPrincipalUtils;
    // Properties
    protected final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public Username getCurrentUsername() {
        return Username.of(this.securityPrincipalUtils.getAuthenticatedUsername());
    }

    @Override
    public JwtUser getCurrentJwtUser() {
        return this.securityPrincipalUtils.getAuthenticatedJwtUser();
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
                user.passwordChangeRequired(),
                user.authorities(),
                attributes
        );
    }

    @Override
    public UserSession getCurrentUserSession(HttpServletRequest httpServletRequest) throws AccessTokenNotFoundException {
        var cookie = this.tokensProvider.readRequestAccessToken(httpServletRequest);
        return this.usersSessionsRepository.isPresent(JwtAccessToken.of(cookie.value())).value();
    }

    @Override
    public ResponseUserSessionsTable getCurrentUserDbSessionsTable(RequestAccessToken requestAccessToken) {
        var username = this.getCurrentUsername();
        this.sessionRegistry.cleanByExpiredRefreshTokens(Set.of(username));
        return this.sessionRegistry.getSessionsTable(username, requestAccessToken);
    }
}
