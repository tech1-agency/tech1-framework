package io.tech1.framework.b2b.base.security.jwt.validators.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserRegistration1;
import io.tech1.framework.b2b.base.security.jwt.domain.events.EventRegistration1Failure;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbInvitationCodesRepository;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbUsersRepository;
import io.tech1.framework.b2b.base.security.jwt.tests.contexts.TestsApplicationValidatorsContext;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseRegistrationRequestsValidator;
import io.tech1.framework.b2b.base.security.jwt.validators.abtracts.AbstractBaseRegistrationRequestsValidator;
import io.tech1.framework.domain.exceptions.authentication.RegistrationException;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1Failure;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.ArrayList;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AbstractBaseRegistrationRequestsValidatorTest {

    @Configuration
    @Import({
            TestsApplicationValidatorsContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final AnyDbInvitationCodesRepository invitationCodesRepository;
        private final AnyDbUsersRepository usersRepository;
        private final SecurityJwtPublisher securityJwtPublisher;
        private final SecurityJwtIncidentPublisher securityJwtIncidentPublisher;

        @Bean
        BaseRegistrationRequestsValidator baseInvitationCodesRequestsValidator() {
            return new AbstractBaseRegistrationRequestsValidator(
                    this.securityJwtPublisher,
                    this.securityJwtIncidentPublisher,
                    this.invitationCodesRepository,
                    this.usersRepository
            ) {};
        }
    }

    private final SecurityJwtPublisher securityJwtPublisher;
    private final SecurityJwtIncidentPublisher securityJwtIncidentPublisher;
    private final AnyDbInvitationCodesRepository invitationCodesRepository;
    private final AnyDbUsersRepository usersRepository;

    private final BaseRegistrationRequestsValidator componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.securityJwtPublisher,
                this.securityJwtIncidentPublisher,
                this.invitationCodesRepository,
                this.usersRepository
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.securityJwtIncidentPublisher,
                this.securityJwtPublisher,
                this.invitationCodesRepository,
                this.usersRepository
        );
    }

    @Test
    void validateRegistrationRequest1UsernameAlreadyUsedTest() {
        // Arrange
        var username = randomUsername();
        var invitationCode = randomString();
        var requestUserRegistration1 = new RequestUserRegistration1(
                username,
                randomPassword(),
                randomPassword(),
                randomZoneId().getId(),
                invitationCode
        );
        var currentDbUser = entity(JwtUser.class);
        when(this.usersRepository.findByUsernameAsJwtUser(username)).thenReturn(currentDbUser);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateRegistrationRequest1(requestUserRegistration1));

        // Assert
        assertThat(throwable)
                .isInstanceOf(RegistrationException.class)
                .hasMessage("Username is already used");
        assertThat(throwable.getMessage()).isEqualTo("Username is already used");
        verify(this.usersRepository).findByUsernameAsJwtUser(username);
        verify(this.securityJwtPublisher).publishRegistration1Failure(
                EventRegistration1Failure.of(
                        username,
                        invitationCode,
                        "Username is already used"
                )
        );
        verify(this.securityJwtIncidentPublisher).publishRegistration1Failure(
                IncidentRegistration1Failure.of(
                        username,
                        invitationCode,
                        "Username is already used"
                )
        );
    }

    @Test
    void validateRegistrationRequest1InvitationCodeAlreadyUsedTest() {
        // Arrange
        var username = randomUsername();
        var invitationCode = randomString();
        var requestUserRegistration1 = new RequestUserRegistration1(
                username,
                randomPassword(),
                randomPassword(),
                randomZoneId().getId(),
                invitationCode
        );
        var dbInvitationCode = entity(AnyDbInvitationCode.class);
        when(this.usersRepository.findByUsernameAsJwtUser(username)).thenReturn(null);
        when(this.invitationCodesRepository.findByValueAsAny(invitationCode)).thenReturn(dbInvitationCode);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateRegistrationRequest1(requestUserRegistration1));

        // Assert
        assertThat(throwable).isInstanceOf(RegistrationException.class);
        assertThat(throwable.getMessage()).isEqualTo("InvitationCode is already used");
        verify(this.usersRepository).findByUsernameAsJwtUser(username);
        verify(this.invitationCodesRepository).findByValueAsAny(invitationCode);
        verify(this.securityJwtPublisher).publishRegistration1Failure(
                new EventRegistration1Failure(
                        username,
                        invitationCode,
                        dbInvitationCode.owner(),
                        "InvitationCode is already used"
                )
        );
        verify(this.securityJwtIncidentPublisher).publishRegistration1Failure(
                new IncidentRegistration1Failure(
                        username,
                        invitationCode,
                        dbInvitationCode.owner(),
                        "InvitationCode is already used"
                )
        );
    }

    @Test
    void validateRegistrationRequest1NoInvitationCodeTest() {
        // Arrange
        var username = randomUsername();
        var invitationCode = randomString();
        var requestUserRegistration1 = new RequestUserRegistration1(
                username,
                randomPassword(),
                randomPassword(),
                randomZoneId().getId(),
                invitationCode
        );
        when(this.usersRepository.findByUsernameAsJwtUser(username)).thenReturn(null);
        when(this.invitationCodesRepository.findByValueAsAny(invitationCode)).thenReturn(null);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateRegistrationRequest1(requestUserRegistration1));

        // Assert
        assertThat(throwable)
                .isInstanceOf(RegistrationException.class)
                .hasMessage("InvitationCode is not found");
        verify(this.usersRepository).findByUsernameAsJwtUser(username);
        verify(this.invitationCodesRepository).findByValueAsAny(invitationCode);
        verify(this.securityJwtPublisher).publishRegistration1Failure(
                EventRegistration1Failure.of(
                        username,
                        invitationCode,
                        "InvitationCode is not found"
                )
        );
        verify(this.securityJwtIncidentPublisher).publishRegistration1Failure(
                IncidentRegistration1Failure.of(
                        username,
                        invitationCode,
                        "InvitationCode is not found"
                )
        );
    }

    @Test
    void validateRegistrationRequest1InvitationCodePresentTest() throws RegistrationException {
        // Arrange
        var username = randomUsername();
        var invitationCode = randomString();
        var requestUserRegistration1 = new RequestUserRegistration1(
                username,
                randomPassword(),
                randomPassword(),
                randomZoneId().getId(),
                invitationCode
        );
        var dbInvitationCode = new AnyDbInvitationCode(
                entity(InvitationCodeId.class),
                randomUsername(),
                new ArrayList<>(),
                randomString(),
                null
        );
        when(this.usersRepository.findByUsernameAsJwtUser(username)).thenReturn(null);
        when(this.invitationCodesRepository.findByValueAsAny(invitationCode)).thenReturn(dbInvitationCode);

        // Act
        this.componentUnderTest.validateRegistrationRequest1(requestUserRegistration1);

        // Assert
        verify(this.usersRepository).findByUsernameAsJwtUser(username);
        verify(this.invitationCodesRepository).findByValueAsAny(invitationCode);
    }
}
