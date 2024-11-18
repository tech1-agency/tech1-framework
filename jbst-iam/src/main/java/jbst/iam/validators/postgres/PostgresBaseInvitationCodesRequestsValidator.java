package jbst.iam.validators.postgres;

import jbst.iam.repositories.postgres.PostgresInvitationsRepository;
import jbst.iam.validators.abtracts.AbstractBaseInvitationCodesRequestsValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jbst.foundation.domain.properties.JbstProperties;

@Slf4j
@Component
public class PostgresBaseInvitationCodesRequestsValidator extends AbstractBaseInvitationCodesRequestsValidator {

    @Autowired
    public PostgresBaseInvitationCodesRequestsValidator(
            PostgresInvitationsRepository invitationCodesRepository,
            JbstProperties jbstProperties
    ) {
        super(
                invitationCodesRepository,
                jbstProperties
        );
    }
}
