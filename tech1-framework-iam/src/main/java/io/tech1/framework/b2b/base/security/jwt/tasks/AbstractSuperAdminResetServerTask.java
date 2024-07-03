package io.tech1.framework.b2b.base.security.jwt.tasks;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.foundation.domain.system.reset_server.ResetServerStatus;
import io.tech1.framework.foundation.incidents.events.publishers.IncidentPublisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static io.tech1.framework.foundation.domain.constants.FrameworkLogsConstants.SERVER_RESET_SERVER_TASK;
import static io.tech1.framework.foundation.domain.enums.Status.COMPLETED;
import static io.tech1.framework.foundation.domain.enums.Status.STARTED;

@Slf4j
@AllArgsConstructor
public abstract class AbstractSuperAdminResetServerTask {

    protected final IncidentPublisher incidentPublisher;

    public abstract ResetServerStatus getStatus();
    public abstract void resetOnServer(JwtUser initiator);

    public final void reset(JwtUser initiator) {
        if (this.getStatus().getState().isResetting()) {
            return;
        }
        LOGGER.info(SERVER_RESET_SERVER_TASK, initiator.username(), STARTED);

        try {
            this.resetOnServer(initiator);
        } catch (RuntimeException ex) {
            this.incidentPublisher.publishThrowable(ex);
        }

        LOGGER.info(SERVER_RESET_SERVER_TASK, initiator.username(), COMPLETED);
    }
}
