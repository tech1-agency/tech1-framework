package io.tech1.framework.iam.server.tasks;

import io.tech1.framework.iam.domain.jwt.JwtUser;
import io.tech1.framework.iam.tasks.AbstractSuperAdminResetServerTask;
import io.tech1.framework.foundation.domain.system.reset_server.ResetServerStatus;
import io.tech1.framework.foundation.incidents.events.publishers.IncidentPublisher;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Component
public class ResetServerTask extends AbstractSuperAdminResetServerTask {

    private final ResetServerStatus status = new ResetServerStatus(1);

    @Autowired
    public ResetServerTask(
            IncidentPublisher incidentPublisher
    ) {
        super(
                incidentPublisher
        );
    }

    @Override
    public void resetOnServer(JwtUser initiator) {
        this.status.complete(initiator.zoneId());
    }
}
