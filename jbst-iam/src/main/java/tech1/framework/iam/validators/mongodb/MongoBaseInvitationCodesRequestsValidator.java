package tech1.framework.iam.validators.mongodb;

import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import tech1.framework.iam.repositories.mongodb.MongoInvitationCodesRepository;
import tech1.framework.iam.validators.abtracts.AbstractBaseInvitationCodesRequestsValidator;
import org.springframework.stereotype.Component;

@Component
public class MongoBaseInvitationCodesRequestsValidator extends AbstractBaseInvitationCodesRequestsValidator {

    public MongoBaseInvitationCodesRequestsValidator(
            MongoInvitationCodesRepository invitationCodesRepository,
            ApplicationFrameworkProperties applicationFrameworkProperties
    ) {
        super(
                invitationCodesRepository,
                applicationFrameworkProperties
        );
    }
}
