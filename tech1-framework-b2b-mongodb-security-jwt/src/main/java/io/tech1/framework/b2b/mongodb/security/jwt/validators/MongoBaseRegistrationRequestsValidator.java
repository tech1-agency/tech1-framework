package io.tech1.framework.b2b.mongodb.security.jwt.validators;

import io.tech1.framework.iam.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.iam.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.iam.validators.abtracts.AbstractBaseRegistrationRequestsValidator;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoInvitationCodesRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Component
public class MongoBaseRegistrationRequestsValidator extends AbstractBaseRegistrationRequestsValidator {

    @Autowired
    public MongoBaseRegistrationRequestsValidator(
            SecurityJwtPublisher securityJwtPublisher,
            SecurityJwtIncidentPublisher securityJwtIncidentPublisher,
            MongoInvitationCodesRepository invitationCodesRepository,
            MongoUsersRepository usersRepository
    ) {
        super(
                securityJwtPublisher,
                securityJwtIncidentPublisher,
                invitationCodesRepository,
                usersRepository
        );
    }
}
