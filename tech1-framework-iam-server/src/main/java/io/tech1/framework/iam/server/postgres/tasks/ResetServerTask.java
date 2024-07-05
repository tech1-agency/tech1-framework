package io.tech1.framework.iam.server.postgres.tasks;

import io.tech1.framework.foundation.domain.system.reset_server.ResetServerStatus;
import io.tech1.framework.foundation.incidents.events.publishers.IncidentPublisher;
import io.tech1.framework.iam.domain.jwt.JwtUser;
import io.tech1.framework.iam.tasks.AbstractSuperAdminResetServerTask;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Getter
@Profile("postgres")
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
