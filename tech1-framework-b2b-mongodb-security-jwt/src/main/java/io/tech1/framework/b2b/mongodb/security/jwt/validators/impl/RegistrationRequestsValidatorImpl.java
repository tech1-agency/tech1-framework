package io.tech1.framework.b2b.mongodb.security.jwt.validators.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserRegistration1;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.InvitationCodeRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.UserRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.validators.RegistrationRequestsValidator;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.exceptions.authentication.RegistrationException;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1Failure;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullNotBlankOrThrow;
import static io.tech1.framework.domain.asserts.Asserts.assertZoneIdOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.*;
import static java.util.Objects.nonNull;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RegistrationRequestsValidatorImpl implements RegistrationRequestsValidator {

    // Publishers
    private final IncidentPublisher incidentPublisher;
    // Repositories
    private final InvitationCodeRepository invitationCodeRepository;
    private final UserRepository userRepository;

    @Override
    public void validateRegistrationRequest1(RequestUserRegistration1 requestUserRegistration1) throws RegistrationException {
        var username = requestUserRegistration1.getUsername();
        var zoneId = requestUserRegistration1.getZoneId();
        var password = requestUserRegistration1.getPassword();
        var confirmPassword = requestUserRegistration1.getConfirmPassword();
        var invitationCode = requestUserRegistration1.getInvitationCode();

        assertNonNullNotBlankOrThrow(username, invalidAttribute("username"));
        assertNonNullNotBlankOrThrow(password, invalidAttribute("password"));
        assertNonNullNotBlankOrThrow(confirmPassword, invalidAttribute("confirmPassword"));
        assertNonNullNotBlankOrThrow(invitationCode, invalidAttribute("invitationCode"));

        assertZoneIdOrThrow(zoneId, invalidAttribute("zoneId"));

        var user = this.userRepository.findByUsername(Username.of(username));
        if (nonNull(user)) {
            var exception = entityAlreadyUsed("Username");
            this.incidentPublisher.publishRegistration1Failure(
                    IncidentRegistration1Failure.of(
                            Username.of(username),
                            exception,
                            invitationCode
                    )
            );
            throw new RegistrationException(exception);
        }

        var dbInvitationCode = this.invitationCodeRepository.findByValue(invitationCode);
        if (nonNull(dbInvitationCode)) {
            if (nonNull(dbInvitationCode.getInvited())) {
                var exception = entityAlreadyUsed("InvitationCode");
                this.incidentPublisher.publishRegistration1Failure(
                        IncidentRegistration1Failure.of(
                                Username.of(username),
                                exception,
                                invitationCode
                        )
                );
                throw new RegistrationException(exception);
            }
        } else {
            var exception = entityNotFoundShort("InvitationCode");
            this.incidentPublisher.publishRegistration1Failure(
                    IncidentRegistration1Failure.of(
                            Username.of(username),
                            exception,
                            invitationCode
                    )
            );
            throw new RegistrationException(exception);
        }
    }
}
