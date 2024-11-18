package tech1.framework.iam.validators.postgres;

import tech1.framework.iam.events.publishers.SecurityJwtIncidentPublisher;
import tech1.framework.iam.events.publishers.SecurityJwtPublisher;
import tech1.framework.iam.validators.abtracts.AbstractBaseRegistrationRequestsValidator;
import tech1.framework.iam.repositories.postgres.PostgresInvitationCodesRepository;
import tech1.framework.iam.repositories.postgres.PostgresUsersRepository;
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
            PostgresInvitationCodesRepository invitationCodesRepository,
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
