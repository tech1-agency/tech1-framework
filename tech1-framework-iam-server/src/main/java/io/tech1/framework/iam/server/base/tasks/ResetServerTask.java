package io.tech1.framework.iam.server.base.tasks;

import io.tech1.framework.foundation.domain.base.Username;
import io.tech1.framework.foundation.domain.system.reset_server.ResetServerStatus;
import io.tech1.framework.foundation.incidents.events.publishers.IncidentPublisher;
import io.tech1.framework.iam.domain.jwt.JwtUser;
import io.tech1.framework.iam.tasks.AbstractSuperAdminResetServerTask;
import io.tech1.framework.iam.template.WssMessagingTemplate;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.tech1.framework.foundation.domain.constants.FrameworkLogsConstants.SERVER_RESET_SERVER_TASK;
import static io.tech1.framework.foundation.domain.enums.Status.FAILURE;
import static io.tech1.framework.foundation.utilities.concurrent.SleepUtility.sleepMilliseconds;
import static io.tech1.framework.iam.domain.events.WebsocketEvent.resetServerProgress;

@Slf4j
@Getter
@Component
public class ResetServerTask extends AbstractSuperAdminResetServerTask {

    // Wss
    private final WssMessagingTemplate wssMessagingTemplate;

    private final ResetServerStatus status = new ResetServerStatus(3);

    @Autowired
    public ResetServerTask(
            IncidentPublisher incidentPublisher,
            WssMessagingTemplate wssMessagingTemplate
    ) {
        super(
                incidentPublisher
        );
        this.wssMessagingTemplate = wssMessagingTemplate;
    }

    @Override
    public void resetOnServer(JwtUser initiator) {
        var username = initiator.username();
        try {
            this.status.reset();

            this.computeAndSendResetServerProgress(username, "[Server] Stage #1");
            this.computeAndSendResetServerProgress(username, "[Server] Stage #2");
            this.computeAndSendResetServerProgress(username, "[Server] Stage #3");

            this.status.complete(initiator.zoneId());
            this.wssMessagingTemplate.sendEventToUser(username, "/account", resetServerProgress(this.status));
        } catch (RuntimeException ex) {
            // WARNING: any exceptions should NOT be expected behaviour, method required ASAP fix
            this.status.setFailureDescription(ex);
            this.wssMessagingTemplate.sendEventToUser(username, "/account", resetServerProgress(this.status));
            LOGGER.error(SERVER_RESET_SERVER_TASK, username, FAILURE);
            this.incidentPublisher.publishThrowable(ex);
        }
    }

    private void computeAndSendResetServerProgress(Username username, String description) {
        this.status.nextStage(description);
        // TODO [YYL] add user destination -> feature-configs + methods as sendHardwareMonitoringProgress() + sendResetServerProgress()
        this.wssMessagingTemplate.sendEventToUser(username, "/account", resetServerProgress(this.status));
        LOGGER.info(SERVER_RESET_SERVER_TASK, description, this.status.getPercentage().percentage() + "%");
        sleepMilliseconds(1000);
    }
}
