package jbst.iam.validators.mongodb;

import jbst.iam.repositories.mongodb.MongoInvitationsRepository;
import jbst.iam.validators.abtracts.AbstractBaseInvitationCodesRequestsValidator;
import org.springframework.stereotype.Component;
import jbst.foundation.domain.properties.JbstProperties;

@Component
public class MongoBaseInvitationCodesRequestsValidator extends AbstractBaseInvitationCodesRequestsValidator {

    public MongoBaseInvitationCodesRequestsValidator(
            MongoInvitationsRepository invitationCodesRepository,
            JbstProperties jbstProperties
    ) {
        super(
                invitationCodesRepository,
                jbstProperties
        );
    }
}
