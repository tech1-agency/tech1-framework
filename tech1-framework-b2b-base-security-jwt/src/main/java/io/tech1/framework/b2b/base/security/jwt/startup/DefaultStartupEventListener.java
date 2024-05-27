package io.tech1.framework.b2b.base.security.jwt.startup;

import io.tech1.framework.b2b.base.security.jwt.essense.AbstractEssenceConstructor;
import io.tech1.framework.domain.enums.Status;
import io.tech1.framework.domain.enums.Toggle;
import io.tech1.framework.domain.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.FRAMEWORK_B2B_SECURITY_JWT_PREFIX;
import static io.tech1.framework.domain.constants.FrameworkLogsConstants.LINE_SEPARATOR_INTERPUNCT;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultStartupEventListener implements BaseStartupEventListener {
    private static final String STARTUP_MESSAGE = FRAMEWORK_B2B_SECURITY_JWT_PREFIX + " Default startup event listener. Status: `{}`";

    // Essence
    protected final AbstractEssenceConstructor essenceConstructor;
    // Properties
    protected final ApplicationFrameworkProperties applicationFrameworkProperties;

    @Override
    public void onStartup() {
        LOGGER.info(LINE_SEPARATOR_INTERPUNCT);
        LOGGER.info(STARTUP_MESSAGE, Status.STARTED);

        var defaultUsers = this.applicationFrameworkProperties.getSecurityJwtConfigs().getEssenceConfigs().getDefaultUsers();
        LOGGER.info("{} Essence defaultUsers — {}", FRAMEWORK_B2B_SECURITY_JWT_PREFIX, Toggle.of(defaultUsers.isEnabled()));
        if (defaultUsers.isEnabled()) {
            this.essenceConstructor.addDefaultUsers();
        }

        var invitationCodes = this.applicationFrameworkProperties.getSecurityJwtConfigs().getEssenceConfigs().getInvitationCodes();
        LOGGER.info("{} Essence invitationCodes — {}", FRAMEWORK_B2B_SECURITY_JWT_PREFIX, Toggle.of(invitationCodes.isEnabled()));
        if (invitationCodes.isEnabled()) {
            this.essenceConstructor.addDefaultUsersInvitationCodes();
        }

        LOGGER.info(STARTUP_MESSAGE, Status.COMPLETED);
        LOGGER.info(LINE_SEPARATOR_INTERPUNCT);
    }
}
