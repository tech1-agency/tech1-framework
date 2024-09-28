package tech1.framework.iam.validators.abtracts;

import tech1.framework.iam.domain.dto.requests.RequestUserRegistration1;
import tech1.framework.iam.domain.events.EventRegistration1Failure;
import tech1.framework.iam.events.publishers.SecurityJwtIncidentPublisher;
import tech1.framework.iam.events.publishers.SecurityJwtPublisher;
import tech1.framework.iam.repositories.InvitationCodesRepository;
import tech1.framework.iam.repositories.UsersRepository;
import tech1.framework.iam.validators.BaseRegistrationRequestsValidator;
import tech1.framework.foundation.domain.exceptions.authentication.RegistrationException;
import tech1.framework.foundation.incidents.domain.registration.IncidentRegistration1Failure;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static tech1.framework.foundation.utilities.exceptions.ExceptionsMessagesUtility.entityAlreadyUsed;
import static tech1.framework.foundation.utilities.exceptions.ExceptionsMessagesUtility.entityNotFound;
import static java.util.Objects.nonNull;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractBaseRegistrationRequestsValidator implements BaseRegistrationRequestsValidator {

    // Publishers
    protected final SecurityJwtPublisher securityJwtPublisher;
    protected final SecurityJwtIncidentPublisher securityJwtIncidentPublisher;
    // Repositories
    protected final InvitationCodesRepository invitationCodesRepository;
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
                            request.invitationCode(),
                            message
                    )
            );
            this.securityJwtIncidentPublisher.publishRegistration1Failure(
                    IncidentRegistration1Failure.of(
                            request.username(),
                            request.invitationCode(),
                            message
                    )
            );
            throw new RegistrationException(message);
        }

        var invitationCode = this.invitationCodesRepository.findByValueAsAny(request.invitationCode());
        if (nonNull(invitationCode)) {
            if (nonNull(invitationCode.invited())) {
                var message = entityAlreadyUsed("Invitation code", invitationCode.value());
                this.securityJwtPublisher.publishRegistration1Failure(
                        new EventRegistration1Failure(
                                request.username(),
                                request.invitationCode(),
                                invitationCode.owner(),
                                message
                        )
                );
                this.securityJwtIncidentPublisher.publishRegistration1Failure(
                        new IncidentRegistration1Failure(
                                request.username(),
                                request.invitationCode(),
                                invitationCode.owner(),
                                message
                        )
                );
                throw new RegistrationException(message);
            }
        } else {
            var exception = entityNotFound("Invitation code", request.invitationCode());
            this.securityJwtPublisher.publishRegistration1Failure(
                    EventRegistration1Failure.of(
                            request.username(),
                            request.invitationCode(),
                            exception
                    )
            );
            this.securityJwtIncidentPublisher.publishRegistration1Failure(
                    IncidentRegistration1Failure.of(
                            request.username(),
                            request.invitationCode(),
                            exception
                    )
            );
            throw new RegistrationException(exception);
        }
    }
}
