package io.tech1.framework.b2b.base.security.jwt.validators.abtracts;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserRegistration1;
import io.tech1.framework.b2b.base.security.jwt.domain.events.EventRegistration1Failure;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbInvitationCodesRepository;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbUsersRepository;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseRegistrationRequestsValidator;
import io.tech1.framework.domain.exceptions.authentication.RegistrationException;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1Failure;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import static io.tech1.framework.domain.asserts.Asserts.*;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.*;
import static java.util.Objects.nonNull;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractBaseRegistrationRequestsValidator implements BaseRegistrationRequestsValidator {

    // Publishers
    protected final SecurityJwtPublisher securityJwtPublisher;
    protected final SecurityJwtIncidentPublisher securityJwtIncidentPublisher;
    // Repositories
    protected final AnyDbInvitationCodesRepository anyDbInvitationCodesRepository;
    protected final AnyDbUsersRepository mongoUsersRepository;

    @Override
    public void validateRegistrationRequest1(RequestUserRegistration1 requestUserRegistration1) throws RegistrationException {
        var username = requestUserRegistration1.username();
        var zoneId = requestUserRegistration1.zoneId();
        var password = requestUserRegistration1.password();
        var confirmPassword = requestUserRegistration1.confirmPassword();
        var invitationCode = requestUserRegistration1.invitationCode();

        assertNonNullOrThrow(username, invalidAttribute("username"));
        assertNonNullOrThrow(password, invalidAttribute("password"));
        assertNonNullOrThrow(confirmPassword, invalidAttribute("confirmPassword"));
        assertNonNullNotBlankOrThrow(invitationCode, invalidAttribute("invitationCode"));

        assertZoneIdOrThrow(zoneId, invalidAttribute("zoneId"));

        var user = this.mongoUsersRepository.findByUsernameAsJwtUser(username);
        if (nonNull(user)) {
            var exception = entityAlreadyUsed("Username", username.identifier());
            this.securityJwtPublisher.publishRegistration1Failure(
                    EventRegistration1Failure.of(
                            username,
                            invitationCode,
                            exception
                    )
            );
            this.securityJwtIncidentPublisher.publishRegistration1Failure(
                    IncidentRegistration1Failure.of(
                            username,
                            invitationCode,
                            exception
                    )
            );
            throw new RegistrationException(exception);
        }

        var dbInvitationCode = this.anyDbInvitationCodesRepository.findByValueAsAny(invitationCode);
        if (nonNull(dbInvitationCode)) {
            if (nonNull(dbInvitationCode.invited())) {
                var exception = entityAlreadyUsed("InvitationCode", dbInvitationCode.value());
                this.securityJwtPublisher.publishRegistration1Failure(
                        new EventRegistration1Failure(
                                username,
                                invitationCode,
                                dbInvitationCode.owner(),
                                exception
                        )
                );
                this.securityJwtIncidentPublisher.publishRegistration1Failure(
                        new IncidentRegistration1Failure(
                                username,
                                invitationCode,
                                dbInvitationCode.owner(),
                                exception
                        )
                );
                throw new RegistrationException(exception);
            }
        } else {
            var exception = entityNotFound("InvitationCode", invitationCode);
            this.securityJwtPublisher.publishRegistration1Failure(
                    EventRegistration1Failure.of(
                            username,
                            invitationCode,
                            exception
                    )
            );
            this.securityJwtIncidentPublisher.publishRegistration1Failure(
                    IncidentRegistration1Failure.of(
                            username,
                            invitationCode,
                            exception
                    )
            );
            throw new RegistrationException(exception);
        }
    }
}
