package io.tech1.framework.iam.assistants.current.base;

import io.tech1.framework.foundation.domain.base.Username;
import io.tech1.framework.foundation.domain.exceptions.tokens.AccessTokenNotFoundException;
import io.tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import io.tech1.framework.foundation.services.hardware.store.HardwareMonitoringStore;
import io.tech1.framework.iam.assistants.current.CurrentSessionAssistant;
import io.tech1.framework.iam.domain.db.UserSession;
import io.tech1.framework.iam.domain.dto.responses.ResponseUserSessionsTable;
import io.tech1.framework.iam.domain.jwt.JwtAccessToken;
import io.tech1.framework.iam.domain.jwt.JwtUser;
import io.tech1.framework.iam.domain.jwt.RequestAccessToken;
import io.tech1.framework.iam.domain.security.CurrentClientUser;
import io.tech1.framework.iam.repositories.UsersSessionsRepository;
import io.tech1.framework.iam.sessions.SessionRegistry;
import io.tech1.framework.iam.tokens.facade.TokensProvider;
import io.tech1.framework.iam.utils.SecurityPrincipalUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Set;

import static java.util.Objects.nonNull;

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
