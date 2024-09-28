package io.tech1.framework.iam.services.postgres;

import io.tech1.framework.iam.services.abstracts.AbstractBaseInvitationCodesService;
import io.tech1.framework.iam.repositories.postgres.PostgresInvitationCodesRepository;
import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PostgresBaseInvitationCodesService extends AbstractBaseInvitationCodesService {

    @Autowired
    public PostgresBaseInvitationCodesService(
            PostgresInvitationCodesRepository invitationCodesRepository,
            ApplicationFrameworkProperties applicationFrameworkProperties
    ) {
        super(
                invitationCodesRepository,
                applicationFrameworkProperties
        );
    }
}
