package io.tech1.framework.b2b.postgres.security.jwt.validators;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserRegistration1;
import io.tech1.framework.b2b.base.security.jwt.domain.events.EventRegistration1Failure;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseRegistrationRequestsValidator;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresInvitationCodesRepository;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresUsersRepository;
import io.tech1.framework.domain.exceptions.authentication.RegistrationException;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1Failure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.tech1.framework.domain.asserts.Asserts.*;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.*;
import static java.util.Objects.nonNull;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PostgresBaseRegistrationRequestsValidator implements BaseRegistrationRequestsValidator {

    // Publishers
    private final SecurityJwtPublisher securityJwtPublisher;
    private final SecurityJwtIncidentPublisher securityJwtIncidentPublisher;
    // Repositories
    private final PostgresInvitationCodesRepository postgresInvitationCodesRepository;
    private final PostgresUsersRepository postgresUsersRepository;

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

        var user = this.postgresUsersRepository.findByUsername(username);
        if (nonNull(user)) {
            var exception = entityAlreadyUsed("Username");
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

        var dbInvitationCode = this.postgresInvitationCodesRepository.findByValue(invitationCode);
        if (nonNull(dbInvitationCode)) {
            if (nonNull(dbInvitationCode.getInvited())) {
                var exception = entityAlreadyUsed("InvitationCode");
                this.securityJwtPublisher.publishRegistration1Failure(
                        new EventRegistration1Failure(
                                username,
                                invitationCode,
                                dbInvitationCode.getOwner(),
                                exception
                        )
                );
                this.securityJwtIncidentPublisher.publishRegistration1Failure(
                        new IncidentRegistration1Failure(
                                username,
                                invitationCode,
                                dbInvitationCode.getOwner(),
                                exception
                        )
                );
                throw new RegistrationException(exception);
            }
        } else {
            var exception = entityNotFoundShort("InvitationCode");
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
