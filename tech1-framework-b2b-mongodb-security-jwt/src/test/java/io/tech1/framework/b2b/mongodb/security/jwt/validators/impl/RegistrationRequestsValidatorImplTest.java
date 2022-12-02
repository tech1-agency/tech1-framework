package io.tech1.framework.b2b.mongodb.security.jwt.validators.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbInvitationCode;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserRegistration1;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.InvitationCodeRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.UserRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.tests.contexts.TestsApplicationValidatorsContext;
import io.tech1.framework.b2b.mongodb.security.jwt.validators.RegistrationRequestsValidator;
import io.tech1.framework.domain.exceptions.authentication.RegistrationException;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1Failure;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RegistrationRequestsValidatorImplTest {

    @Configuration
    @Import({
            TestsApplicationValidatorsContext.class
    })
    static class ContextConfiguration {

    }

    private final IncidentPublisher incidentPublisher;
    private final InvitationCodeRepository invitationCodeRepository;
    private final UserRepository userRepository;

    private final RegistrationRequestsValidator componentUnderTest;

    @BeforeEach
    public void beforeEach() {
        reset(
                this.incidentPublisher,
                this.invitationCodeRepository,
                this.userRepository
        );
    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(
                this.incidentPublisher,
                this.invitationCodeRepository,
                this.userRepository
        );
    }

    @Test
    public void validateRegistrationRequest1UsernameAlreadyUsedTest() {
        // Arrange
        var username = randomUsername();
        var password = randomString();
        var confirmPassword = randomString();
        var zoneId = randomZoneId().getId();
        var invitationCode = randomString();
        var requestUserRegistration1 = new RequestUserRegistration1(
                username.getIdentifier(),
                password,
                confirmPassword,
                zoneId,
                invitationCode
        );
        var currentDbUser = entity(DbUser.class);
        when(this.userRepository.findByUsername(eq(username))).thenReturn(currentDbUser);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateRegistrationRequest1(requestUserRegistration1));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(RegistrationException.class);
        assertThat(throwable.getMessage()).isEqualTo("Username is already used");
        verify(this.userRepository).findByUsername(eq(username));
        verify(this.incidentPublisher).publishRegistration1Failure(eq(
                IncidentRegistration1Failure.of(
                        username,
                        "Username is already used",
                        invitationCode
                )
        ));
    }

    @Test
    public void validateRegistrationRequest1InvitationCodeAlreadyUsedTest() {
        // Arrange
        var username = randomUsername();
        var password = randomString();
        var confirmPassword = randomString();
        var zoneId = randomZoneId().getId();
        var invitationCode = randomString();
        var requestUserRegistration1 = new RequestUserRegistration1(
                username.getIdentifier(),
                password,
                confirmPassword,
                zoneId,
                invitationCode
        );
        var dbInvitationCode = entity(DbInvitationCode.class);
        when(this.userRepository.findByUsername(eq(username))).thenReturn(null);
        when(this.invitationCodeRepository.findByValue(eq(invitationCode))).thenReturn(dbInvitationCode);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateRegistrationRequest1(requestUserRegistration1));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(RegistrationException.class);
        assertThat(throwable.getMessage()).isEqualTo("InvitationCode is already used");
        verify(this.userRepository).findByUsername(eq(username));
        verify(this.invitationCodeRepository).findByValue(eq(invitationCode));
        verify(this.incidentPublisher).publishRegistration1Failure(eq(
                IncidentRegistration1Failure.of(
                        username,
                        "InvitationCode is already used",
                        invitationCode
                )
        ));
    }

    @Test
    public void validateRegistrationRequest1NoInvitationCodeTest() {
        // Arrange
        var username = randomUsername();
        var password = randomString();
        var confirmPassword = randomString();
        var zoneId = randomZoneId().getId();
        var invitationCode = randomString();
        var requestUserRegistration1 = new RequestUserRegistration1(
                username.getIdentifier(),
                password,
                confirmPassword,
                zoneId,
                invitationCode
        );
        when(this.userRepository.findByUsername(eq(username))).thenReturn(null);
        when(this.invitationCodeRepository.findByValue(eq(invitationCode))).thenReturn(null);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateRegistrationRequest1(requestUserRegistration1));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(RegistrationException.class);
        assertThat(throwable.getMessage()).isEqualTo("InvitationCode is not found");
        verify(this.userRepository).findByUsername(eq(username));
        verify(this.invitationCodeRepository).findByValue(eq(invitationCode));
        verify(this.incidentPublisher).publishRegistration1Failure(eq(
                IncidentRegistration1Failure.of(
                        username,
                        "InvitationCode is not found",
                        invitationCode
                )
        ));
    }

    @Test
    public void validateRegistrationRequest1InvitationCodePresentTest() throws RegistrationException {
        // Arrange
        var username = randomUsername();
        var password = randomString();
        var confirmPassword = randomString();
        var zoneId = randomZoneId().getId();
        var invitationCode = randomString();
        var requestUserRegistration1 = new RequestUserRegistration1(
                username.getIdentifier(),
                password,
                confirmPassword,
                zoneId,
                invitationCode
        );
        var dbInvitationCode = entity(DbInvitationCode.class);
        dbInvitationCode.setInvited(null);
        when(this.userRepository.findByUsername(eq(username))).thenReturn(null);
        when(this.invitationCodeRepository.findByValue(eq(invitationCode))).thenReturn(dbInvitationCode);

        // Act
        this.componentUnderTest.validateRegistrationRequest1(requestUserRegistration1);

        // Assert
        verify(this.userRepository).findByUsername(eq(username));
        verify(this.invitationCodeRepository).findByValue(eq(invitationCode));
    }
}
