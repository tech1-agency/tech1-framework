package io.tech1.framework.b2b.base.security.jwt.validators.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.db.InvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserRegistration1;
import io.tech1.framework.b2b.base.security.jwt.domain.events.EventRegistration1Failure;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.base.security.jwt.repositories.InvitationCodesRepository;
import io.tech1.framework.b2b.base.security.jwt.repositories.UsersRepository;
import io.tech1.framework.b2b.base.security.jwt.tests.contexts.TestsApplicationValidatorsContext;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseRegistrationRequestsValidator;
import io.tech1.framework.b2b.base.security.jwt.validators.abtracts.AbstractBaseRegistrationRequestsValidator;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
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

import java.util.HashSet;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomZoneId;
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
        private final InvitationCodesRepository invitationCodesRepository;
        private final UsersRepository usersRepository;
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
    private final InvitationCodesRepository invitationCodesRepository;
    private final UsersRepository usersRepository;

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
        var username = Username.random();
        var invitationCode = randomString();
        var requestUserRegistration1 = new RequestUserRegistration1(
                username,
                Password.random(),
                Password.random(),
                randomZoneId().getId(),
                invitationCode
        );
        var user = entity(JwtUser.class);
        when(this.usersRepository.findByUsernameAsJwtUserOrNull(username)).thenReturn(user);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateRegistrationRequest1(requestUserRegistration1));

        // Assert
        var exception = "Username: Already Used, id = " + username;
        assertThat(throwable)
                .isInstanceOf(RegistrationException.class)
                .hasMessage(exception);
        verify(this.usersRepository).findByUsernameAsJwtUserOrNull(username);
        verify(this.securityJwtPublisher).publishRegistration1Failure(
                EventRegistration1Failure.of(
                        username,
                        invitationCode,
                        exception
                )
        );
        verify(this.securityJwtIncidentPublisher).publishRegistration1Failure(
                IncidentRegistration1Failure.of(
                        username,
                        invitationCode,
                        exception
                )
        );
    }

    @Test
    void validateRegistrationRequest1InvitationCodeAlreadyUsedTest() {
        // Arrange
        var username = Username.random();
        var invitationCode = randomString();
        var requestUserRegistration1 = new RequestUserRegistration1(
                username,
                Password.random(),
                Password.random(),
                randomZoneId().getId(),
                invitationCode
        );
        var dbInvitationCode = entity(InvitationCode.class);
        when(this.usersRepository.findByUsernameAsJwtUserOrNull(username)).thenReturn(null);
        when(this.invitationCodesRepository.findByValueAsAny(invitationCode)).thenReturn(dbInvitationCode);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateRegistrationRequest1(requestUserRegistration1));

        // Assert
        var exception = "InvitationCode: Already Used, id = " + dbInvitationCode.value();
        assertThat(throwable)
                .isInstanceOf(RegistrationException.class)
                .hasMessage(exception);
        verify(this.usersRepository).findByUsernameAsJwtUserOrNull(username);
        verify(this.invitationCodesRepository).findByValueAsAny(invitationCode);
        verify(this.securityJwtPublisher).publishRegistration1Failure(
                new EventRegistration1Failure(
                        username,
                        invitationCode,
                        dbInvitationCode.owner(),
                        exception
                )
        );
        verify(this.securityJwtIncidentPublisher).publishRegistration1Failure(
                new IncidentRegistration1Failure(
                        username,
                        invitationCode,
                        dbInvitationCode.owner(),
                        exception
                )
        );
    }

    @Test
    void validateRegistrationRequest1NoInvitationCodeTest() {
        // Arrange
        var username = Username.random();
        var invitationCode = randomString();
        var requestUserRegistration1 = new RequestUserRegistration1(
                username,
                Password.random(),
                Password.random(),
                randomZoneId().getId(),
                invitationCode
        );
        when(this.usersRepository.findByUsernameAsJwtUserOrNull(username)).thenReturn(null);
        when(this.invitationCodesRepository.findByValueAsAny(invitationCode)).thenReturn(null);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateRegistrationRequest1(requestUserRegistration1));

        // Assert
        var exception = "InvitationCode: Not Found, id = " + invitationCode;
        assertThat(throwable)
                .isInstanceOf(RegistrationException.class)
                .hasMessage(exception);
        verify(this.usersRepository).findByUsernameAsJwtUserOrNull(username);
        verify(this.invitationCodesRepository).findByValueAsAny(invitationCode);
        verify(this.securityJwtPublisher).publishRegistration1Failure(
                EventRegistration1Failure.of(
                        username,
                        invitationCode,
                        exception
                )
        );
        verify(this.securityJwtIncidentPublisher).publishRegistration1Failure(
                IncidentRegistration1Failure.of(
                        username,
                        invitationCode,
                        exception
                )
        );
    }

    @Test
    void validateRegistrationRequest1InvitationCodePresentTest() throws RegistrationException {
        // Arrange
        var username = Username.random();
        var invitationCode = randomString();
        var requestUserRegistration1 = new RequestUserRegistration1(
                username,
                Password.random(),
                Password.random(),
                randomZoneId().getId(),
                invitationCode
        );
        var dbInvitationCode = new InvitationCode(
                entity(InvitationCodeId.class),
                Username.random(),
                new HashSet<>(),
                randomString(),
                null
        );
        when(this.usersRepository.findByUsernameAsJwtUserOrNull(username)).thenReturn(null);
        when(this.invitationCodesRepository.findByValueAsAny(invitationCode)).thenReturn(dbInvitationCode);

        // Act
        this.componentUnderTest.validateRegistrationRequest1(requestUserRegistration1);

        // Assert
        verify(this.usersRepository).findByUsernameAsJwtUserOrNull(username);
        verify(this.invitationCodesRepository).findByValueAsAny(invitationCode);
    }
}
