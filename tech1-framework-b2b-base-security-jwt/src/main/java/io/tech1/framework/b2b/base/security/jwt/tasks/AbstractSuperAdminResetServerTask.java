package io.tech1.framework.b2b.base.security.jwt.tasks;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.domain.system.reset_server.ResetServerStatus;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static io.tech1.framework.domain.constants.FrameworkLogsConstants.SERVER_RESET_SERVER_TASK;
import static io.tech1.framework.domain.enums.Status.COMPLETED;
import static io.tech1.framework.domain.enums.Status.STARTED;

@Slf4j
@AllArgsConstructor
public abstract class AbstractSuperAdminResetServerTask {

    protected final IncidentPublisher incidentPublisher;

    public abstract ResetServerStatus getStatus();
    public abstract void resetOnServer(JwtUser user);

    public final void reset(JwtUser user) {
        if (this.getStatus().getState().isResetting()) {
            return;
        }
        LOGGER.info(SERVER_RESET_SERVER_TASK, user.username(), STARTED);

        try {
            this.resetOnServer(user);
        } catch (RuntimeException ex) {
            this.incidentPublisher.publishThrowable(ex);
        }

        LOGGER.info(SERVER_RESET_SERVER_TASK, user.username(), COMPLETED);
    }
}
