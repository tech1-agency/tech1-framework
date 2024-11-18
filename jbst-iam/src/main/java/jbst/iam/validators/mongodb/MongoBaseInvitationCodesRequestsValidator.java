package jbst.iam.validators.mongodb;

import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import jbst.iam.repositories.mongodb.MongoInvitationCodesRepository;
import jbst.iam.validators.abtracts.AbstractBaseInvitationCodesRequestsValidator;
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
