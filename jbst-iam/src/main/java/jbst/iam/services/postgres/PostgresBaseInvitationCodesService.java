package jbst.iam.services.postgres;

import jbst.iam.repositories.postgres.PostgresInvitationCodesRepository;
import jbst.iam.services.abstracts.AbstractBaseInvitationCodesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jbst.foundation.domain.properties.ApplicationFrameworkProperties;

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
