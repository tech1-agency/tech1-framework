package jbst.iam.validators.abstracts;

import jbst.iam.domain.db.Invitation;
import jbst.iam.domain.dto.requests.RequestUserRegistration1;
import jbst.iam.domain.events.EventRegistration1Failure;
import jbst.iam.domain.jwt.JwtUser;
import jbst.iam.events.publishers.SecurityJwtIncidentPublisher;
import jbst.iam.events.publishers.SecurityJwtPublisher;
import jbst.iam.repositories.InvitationsRepository;
import jbst.iam.repositories.UsersRepository;
import jbst.iam.configurations.TestConfigurationValidators;
import jbst.iam.validators.BaseRegistrationRequestsValidator;
import jbst.iam.validators.abtracts.AbstractBaseRegistrationRequestsValidator;
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
import jbst.foundation.domain.exceptions.authentication.RegistrationException;
import jbst.foundation.incidents.domain.registration.IncidentRegistration1Failure;
import jbst.foundation.utilities.exceptions.ExceptionsMessagesUtility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AbstractBaseRegistrationRequestsValidatorTest {

    @Configuration
    @Import({
            TestConfigurationValidators.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final InvitationsRepository invitationsRepository;
        private final UsersRepository usersRepository;
        private final SecurityJwtPublisher securityJwtPublisher;
        private final SecurityJwtIncidentPublisher securityJwtIncidentPublisher;

        @Bean
        BaseRegistrationRequestsValidator baseInvitationRequestsValidator() {
            return new AbstractBaseRegistrationRequestsValidator(
                    this.securityJwtPublisher,
                    this.securityJwtIncidentPublisher,
                    this.invitationsRepository,
                    this.usersRepository
            ) {};
        }
    }

    private final SecurityJwtPublisher securityJwtPublisher;
    private final SecurityJwtIncidentPublisher securityJwtIncidentPublisher;
    private final InvitationsRepository invitationsRepository;
    private final UsersRepository usersRepository;

    private final BaseRegistrationRequestsValidator componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.securityJwtPublisher,
                this.securityJwtIncidentPublisher,
                this.invitationsRepository,
                this.usersRepository
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.securityJwtIncidentPublisher,
                this.securityJwtPublisher,
                this.invitationsRepository,
                this.usersRepository
        );
    }

    @Test
    void validateRegistrationRequest1UsernameAlreadyUsedTest() {
        // Arrange
        var request = RequestUserRegistration1.hardcoded();
        when(this.usersRepository.findByUsernameAsJwtUserOrNull(request.username())).thenReturn(JwtUser.hardcoded());

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateRegistrationRequest1(request));

        // Assert
        var exception = ExceptionsMessagesUtility.entityAlreadyUsed("Username", request.username().value());
        assertThat(throwable)
                .isInstanceOf(RegistrationException.class)
                .hasMessage(exception);
        verify(this.usersRepository).findByUsernameAsJwtUserOrNull(request.username());
        verify(this.securityJwtPublisher).publishRegistration1Failure(
                EventRegistration1Failure.of(
                        request.username(),
                        request.invitation(),
                        exception
                )
        );
        verify(this.securityJwtIncidentPublisher).publishRegistration1Failure(
                IncidentRegistration1Failure.of(
                        request.username(),
                        request.invitation(),
                        exception
                )
        );
    }

    @Test
    void validateRegistrationRequest1InvitationCodeAlreadyUsedTest() {
        // Arrange
        var request = RequestUserRegistration1.hardcoded();
        var invitation = Invitation.random();
        when(this.usersRepository.findByUsernameAsJwtUserOrNull(request.username())).thenReturn(null);
        when(this.invitationsRepository.findByValueAsAny(request.invitation())).thenReturn(invitation);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateRegistrationRequest1(request));

        // Assert
        var exception = ExceptionsMessagesUtility.entityAlreadyUsed("Invitation code", invitation.value());
        assertThat(throwable)
                .isInstanceOf(RegistrationException.class)
                .hasMessage(exception);
        verify(this.usersRepository).findByUsernameAsJwtUserOrNull(request.username());
        verify(this.invitationsRepository).findByValueAsAny(request.invitation());
        verify(this.securityJwtPublisher).publishRegistration1Failure(
                new EventRegistration1Failure(
                        request.username(),
                        request.invitation(),
                        invitation.owner(),
                        exception
                )
        );
        verify(this.securityJwtIncidentPublisher).publishRegistration1Failure(
                new IncidentRegistration1Failure(
                        request.username(),
                        request.invitation(),
                        invitation.owner(),
                        exception
                )
        );
    }

    @Test
    void validateRegistrationRequest1NoInvitationCodeTest() {
        // Arrange
        var request = RequestUserRegistration1.hardcoded();
        var username = request.username();
        var invitation = request.invitation();
        when(this.usersRepository.findByUsernameAsJwtUserOrNull(username)).thenReturn(null);
        when(this.invitationsRepository.findByValueAsAny(invitation)).thenReturn(null);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateRegistrationRequest1(request));

        // Assert
        var exception = ExceptionsMessagesUtility.entityNotFound("Invitation code", invitation);
        assertThat(throwable)
                .isInstanceOf(RegistrationException.class)
                .hasMessage(exception);
        verify(this.usersRepository).findByUsernameAsJwtUserOrNull(username);
        verify(this.invitationsRepository).findByValueAsAny(invitation);
        verify(this.securityJwtPublisher).publishRegistration1Failure(
                EventRegistration1Failure.of(
                        username,
                        invitation,
                        exception
                )
        );
        verify(this.securityJwtIncidentPublisher).publishRegistration1Failure(
                IncidentRegistration1Failure.of(
                        username,
                        invitation,
                        exception
                )
        );
    }

    @Test
    void validateRegistrationRequest1InvitationCodePresentTest() throws RegistrationException {
        // Arrange
        var request = RequestUserRegistration1.hardcoded();
        when(this.usersRepository.findByUsernameAsJwtUserOrNull(request.username())).thenReturn(null);
        when(this.invitationsRepository.findByValueAsAny(request.invitation())).thenReturn(Invitation.randomNoInvited());

        // Act
        this.componentUnderTest.validateRegistrationRequest1(request);

        // Assert
        verify(this.usersRepository).findByUsernameAsJwtUserOrNull(request.username());
        verify(this.invitationsRepository).findByValueAsAny(request.invitation());
    }
}
