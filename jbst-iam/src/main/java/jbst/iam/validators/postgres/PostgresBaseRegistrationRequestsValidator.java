package jbst.iam.validators.postgres;

import jbst.iam.events.publishers.SecurityJwtIncidentPublisher;
import jbst.iam.events.publishers.SecurityJwtPublisher;
import jbst.iam.repositories.postgres.PostgresInvitationsRepository;
import jbst.iam.repositories.postgres.PostgresUsersRepository;
import jbst.iam.validators.abtracts.AbstractBaseRegistrationRequestsValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PostgresBaseRegistrationRequestsValidator extends AbstractBaseRegistrationRequestsValidator {

    @Autowired
    public PostgresBaseRegistrationRequestsValidator(
            SecurityJwtPublisher securityJwtPublisher,
            SecurityJwtIncidentPublisher securityJwtIncidentPublisher,
            PostgresInvitationsRepository invitationCodesRepository,
            PostgresUsersRepository usersRepository
    ) {
        super(
                securityJwtPublisher,
                securityJwtIncidentPublisher,
                invitationCodesRepository,
                usersRepository
        );
    }
}
