package io.tech1.framework.b2b.mongodb.security.jwt.validators;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbInvitationCode;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUser;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserRegistration1;
import io.tech1.framework.b2b.base.security.jwt.domain.events.EventRegistration1Failure;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoInvitationCodesRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUsersRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.tests.contexts.TestsApplicationValidatorsContext;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseRegistrationRequestsValidator;
import io.tech1.framework.domain.exceptions.authentication.RegistrationException;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1Failure;
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
class MongoBaseRegistrationRequestsValidatorTest {

    @Configuration
    @Import({
            TestsApplicationValidatorsContext.class
    })
    static class ContextConfiguration {

    }

    private final SecurityJwtPublisher securityJwtPublisher;
    private final SecurityJwtIncidentPublisher securityJwtIncidentPublisher;
    private final MongoInvitationCodesRepository mongoInvitationCodesRepository;
    private final MongoUsersRepository mongoUsersRepository;

    private final BaseRegistrationRequestsValidator componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.securityJwtPublisher,
                this.securityJwtIncidentPublisher,
                this.mongoInvitationCodesRepository,
                this.mongoUsersRepository
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.securityJwtIncidentPublisher,
                this.securityJwtPublisher,
                this.mongoInvitationCodesRepository,
                this.mongoUsersRepository
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
        var currentDbUser = entity(MongoDbUser.class);
        when(this.mongoUsersRepository.findByUsername(username)).thenReturn(currentDbUser);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateRegistrationRequest1(requestUserRegistration1));

        // Assert
        assertThat(throwable)
                .isInstanceOf(RegistrationException.class)
                .hasMessage("Username is already used");
        assertThat(throwable.getMessage()).isEqualTo("Username is already used");
        verify(this.mongoUsersRepository).findByUsername(username);
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
        var dbInvitationCode = entity(MongoDbInvitationCode.class);
        when(this.mongoUsersRepository.findByUsername(username)).thenReturn(null);
        when(this.mongoInvitationCodesRepository.findByValue(invitationCode)).thenReturn(dbInvitationCode);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateRegistrationRequest1(requestUserRegistration1));

        // Assert
        assertThat(throwable).isInstanceOf(RegistrationException.class);
        assertThat(throwable.getMessage()).isEqualTo("InvitationCode is already used");
        verify(this.mongoUsersRepository).findByUsername(username);
        verify(this.mongoInvitationCodesRepository).findByValue(invitationCode);
        verify(this.securityJwtPublisher).publishRegistration1Failure(
                new EventRegistration1Failure(
                        username,
                        invitationCode,
                        dbInvitationCode.getOwner(),
                        "InvitationCode is already used"
                )
        );
        verify(this.securityJwtIncidentPublisher).publishRegistration1Failure(
                new IncidentRegistration1Failure(
                        username,
                        invitationCode,
                        dbInvitationCode.getOwner(),
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
        when(this.mongoUsersRepository.findByUsername(username)).thenReturn(null);
        when(this.mongoInvitationCodesRepository.findByValue(invitationCode)).thenReturn(null);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateRegistrationRequest1(requestUserRegistration1));

        // Assert
        assertThat(throwable)
                .isInstanceOf(RegistrationException.class)
                .hasMessage("InvitationCode is not found");
        verify(this.mongoUsersRepository).findByUsername(username);
        verify(this.mongoInvitationCodesRepository).findByValue(invitationCode);
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
        var dbInvitationCode = entity(MongoDbInvitationCode.class);
        dbInvitationCode.setInvited(null);
        when(this.mongoUsersRepository.findByUsername(username)).thenReturn(null);
        when(this.mongoInvitationCodesRepository.findByValue(invitationCode)).thenReturn(dbInvitationCode);

        // Act
        this.componentUnderTest.validateRegistrationRequest1(requestUserRegistration1);

        // Assert
        verify(this.mongoUsersRepository).findByUsername(username);
        verify(this.mongoInvitationCodesRepository).findByValue(invitationCode);
    }
}
