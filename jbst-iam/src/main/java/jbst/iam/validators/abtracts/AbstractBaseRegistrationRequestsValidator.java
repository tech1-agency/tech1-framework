package jbst.iam.validators.abtracts;

import jbst.iam.domain.dto.requests.RequestUserRegistration1;
import jbst.iam.domain.events.EventRegistration1Failure;
import jbst.iam.events.publishers.SecurityJwtIncidentPublisher;
import jbst.iam.events.publishers.SecurityJwtPublisher;
import jbst.iam.repositories.InvitationsRepository;
import jbst.iam.repositories.UsersRepository;
import jbst.iam.validators.BaseRegistrationRequestsValidator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import jbst.foundation.domain.exceptions.authentication.RegistrationException;
import jbst.foundation.incidents.domain.registration.IncidentRegistration1Failure;

import static java.util.Objects.nonNull;
import static jbst.foundation.utilities.exceptions.ExceptionsMessagesUtility.entityAlreadyUsed;
import static jbst.foundation.utilities.exceptions.ExceptionsMessagesUtility.entityNotFound;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractBaseRegistrationRequestsValidator implements BaseRegistrationRequestsValidator {

    // Publishers
    protected final SecurityJwtPublisher securityJwtPublisher;
    protected final SecurityJwtIncidentPublisher securityJwtIncidentPublisher;
    // Repositories
    protected final InvitationsRepository invitationsRepository;
    protected final UsersRepository mongoUsersRepository;

    @Override
    public void validateRegistrationRequest1(RequestUserRegistration1 request) throws RegistrationException {
        request.assertPasswordsOrThrow();
        var user = this.mongoUsersRepository.findByUsernameAsJwtUserOrNull(request.username());
        if (nonNull(user)) {
            var message = entityAlreadyUsed("Username", request.username().value());
            this.securityJwtPublisher.publishRegistration1Failure(
                    EventRegistration1Failure.of(
                            request.username(),
                            request.code(),
                            message
                    )
            );
            this.securityJwtIncidentPublisher.publishRegistration1Failure(
                    IncidentRegistration1Failure.of(
                            request.username(),
                            request.code(),
                            message
                    )
            );
            throw new RegistrationException(message);
        }

        var invitation = this.invitationsRepository.findByCodeAsAny(request.code());
        if (nonNull(invitation)) {
            if (nonNull(invitation.invited())) {
                var message = entityAlreadyUsed("Code", invitation.code());
                this.securityJwtPublisher.publishRegistration1Failure(
                        new EventRegistration1Failure(
                                request.username(),
                                request.code(),
                                invitation.owner(),
                                message
                        )
                );
                this.securityJwtIncidentPublisher.publishRegistration1Failure(
                        new IncidentRegistration1Failure(
                                request.username(),
                                request.code(),
                                invitation.owner(),
                                message
                        )
                );
                throw new RegistrationException(message);
            }
        } else {
            var exception = entityNotFound("Code", request.code());
            this.securityJwtPublisher.publishRegistration1Failure(
                    EventRegistration1Failure.of(
                            request.username(),
                            request.code(),
                            exception
                    )
            );
            this.securityJwtIncidentPublisher.publishRegistration1Failure(
                    IncidentRegistration1Failure.of(
                            request.username(),
                            request.code(),
                            exception
                    )
            );
            throw new RegistrationException(exception);
        }
    }
}
