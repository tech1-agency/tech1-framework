package jbst.iam.validators.mongodb;

import jbst.iam.events.publishers.SecurityJwtIncidentPublisher;
import jbst.iam.events.publishers.SecurityJwtPublisher;
import jbst.iam.repositories.mongodb.MongoInvitationsRepository;
import jbst.iam.repositories.mongodb.MongoUsersRepository;
import jbst.iam.validators.abtracts.AbstractBaseRegistrationRequestsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MongoBaseRegistrationRequestsValidator extends AbstractBaseRegistrationRequestsValidator {

    @Autowired
    public MongoBaseRegistrationRequestsValidator(
            SecurityJwtPublisher securityJwtPublisher,
            SecurityJwtIncidentPublisher securityJwtIncidentPublisher,
            MongoInvitationsRepository invitationsRepository,
            MongoUsersRepository usersRepository
    ) {
        super(
                securityJwtPublisher,
                securityJwtIncidentPublisher,
                invitationsRepository,
                usersRepository
        );
    }
}
