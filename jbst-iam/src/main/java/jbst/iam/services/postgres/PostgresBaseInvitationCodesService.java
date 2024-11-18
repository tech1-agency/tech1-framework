package jbst.iam.services.postgres;

import jbst.iam.repositories.postgres.PostgresInvitationsRepository;
import jbst.iam.services.abstracts.AbstractBaseInvitationCodesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jbst.foundation.domain.properties.JbstProperties;

@Slf4j
@Service
public class PostgresBaseInvitationCodesService extends AbstractBaseInvitationCodesService {

    @Autowired
    public PostgresBaseInvitationCodesService(
            PostgresInvitationsRepository invitationCodesRepository,
            JbstProperties jbstProperties
    ) {
        super(
                invitationCodesRepository,
                jbstProperties
        );
    }
}
