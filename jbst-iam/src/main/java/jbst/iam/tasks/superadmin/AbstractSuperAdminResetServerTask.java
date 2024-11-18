package jbst.iam.tasks.superadmin;

import jbst.iam.domain.jwt.JwtUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jbst.foundation.domain.system.reset_server.ResetServerStatus;
import jbst.foundation.incidents.events.publishers.IncidentPublisher;

import static jbst.foundation.domain.constants.FrameworkLogsConstants.SERVER_RESET_SERVER_TASK;
import static jbst.foundation.domain.enums.Status.COMPLETED;
import static jbst.foundation.domain.enums.Status.STARTED;

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
