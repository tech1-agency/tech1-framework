package jbst.iam.startup;

import jbst.foundation.domain.constants.JbstConstants;
import jbst.foundation.domain.enums.Status;
import jbst.foundation.domain.enums.Toggle;
import jbst.foundation.domain.properties.JbstProperties;
import jbst.iam.essence.AbstractEssenceConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultStartupEventListener implements BaseStartupEventListener {
    private static final String STARTUP_MESSAGE = JbstConstants.Logs.FRAMEWORK_B2B_SECURITY_JWT_PREFIX + " Default startup event listener. Status: `{}`";

    // Essence
    protected final AbstractEssenceConstructor essenceConstructor;
    // Properties
    protected final JbstProperties jbstProperties;

    @Override
    public void onStartup() {
        LOGGER.info(JbstConstants.Symbols.LINE_SEPARATOR_INTERPUNCT);
        LOGGER.info(STARTUP_MESSAGE, Status.STARTED);

        var defaultUsers = this.jbstProperties.getSecurityJwtConfigs().getEssenceConfigs().getDefaultUsers();
        LOGGER.info("{} Essence defaultUsers — {}", JbstConstants.Logs.FRAMEWORK_B2B_SECURITY_JWT_PREFIX, Toggle.of(defaultUsers.isEnabled()));
        if (defaultUsers.isEnabled()) {
            this.essenceConstructor.addDefaultUsers();
        }

        var invitationCodes = this.jbstProperties.getSecurityJwtConfigs().getEssenceConfigs().getInvitationCodes();
        LOGGER.info("{} Essence invitationCodes — {}", JbstConstants.Logs.FRAMEWORK_B2B_SECURITY_JWT_PREFIX, Toggle.of(invitationCodes.isEnabled()));
        if (invitationCodes.isEnabled()) {
            this.essenceConstructor.addDefaultUsersInvitationCodes();
        }

        LOGGER.info(STARTUP_MESSAGE, Status.COMPLETED);
        LOGGER.info(JbstConstants.Symbols.LINE_SEPARATOR_INTERPUNCT);
    }
}
