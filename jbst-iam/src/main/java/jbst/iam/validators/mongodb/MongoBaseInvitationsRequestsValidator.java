package jbst.iam.validators.mongodb;

import jbst.iam.repositories.mongodb.MongoInvitationsRepository;
import jbst.iam.validators.abtracts.AbstractBaseInvitationsRequestsValidator;
import org.springframework.stereotype.Component;
import jbst.foundation.domain.properties.JbstProperties;

@Component
public class MongoBaseInvitationsRequestsValidator extends AbstractBaseInvitationsRequestsValidator {

    public MongoBaseInvitationsRequestsValidator(
            MongoInvitationsRepository invitationsRepository,
            JbstProperties jbstProperties
    ) {
        super(
                invitationsRepository,
                jbstProperties
        );
    }
}
