package jbst.iam.validators.postgres;

import jbst.iam.validators.abtracts.AbstractBaseInvitationCodesRequestsValidator;
import jbst.iam.repositories.postgres.PostgresInvitationCodesRepository;
import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PostgresBaseInvitationCodesRequestsValidator extends AbstractBaseInvitationCodesRequestsValidator {

    @Autowired
    public PostgresBaseInvitationCodesRequestsValidator(
            PostgresInvitationCodesRepository invitationCodesRepository,
            ApplicationFrameworkProperties applicationFrameworkProperties
    ) {
        super(
                invitationCodesRepository,
                applicationFrameworkProperties
        );
    }
}
