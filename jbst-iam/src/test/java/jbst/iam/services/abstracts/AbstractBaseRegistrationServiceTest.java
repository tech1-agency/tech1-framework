package jbst.iam.services.abstracts;

import jbst.iam.domain.db.Invitation;
import jbst.iam.domain.dto.requests.RequestUserRegistration1;
import jbst.iam.repositories.InvitationsRepository;
import jbst.iam.repositories.UsersRepository;
import jbst.iam.services.BaseRegistrationService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import jbst.foundation.domain.base.Password;
import jbst.foundation.domain.base.Username;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static jbst.foundation.utilities.random.EntityUtility.entity;
import static jbst.foundation.utilities.random.RandomUtility.randomString;
import static jbst.foundation.utilities.random.RandomUtility.randomZoneId;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AbstractBaseRegistrationServiceTest {
    @Configuration
    static class ContextConfiguration {
        @Bean
        InvitationsRepository invitationCodeRepository() {
            return mock(InvitationsRepository.class);
        }

        @Bean
        UsersRepository userRepository() {
            return mock(UsersRepository.class);
        }

        @Bean
        BCryptPasswordEncoder bCryptPasswordEncoder() {
            return mock(BCryptPasswordEncoder.class);
        }

        @Bean
        AbstractBaseRegistrationService registrationService() {
            return new AbstractBaseRegistrationService(
                    this.invitationCodeRepository(),
                    this.userRepository(),
                    this.bCryptPasswordEncoder()
            ) {};
        }
    }

    private final InvitationsRepository invitationsRepository;
    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final BaseRegistrationService componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.invitationsRepository,
                this.usersRepository,
                this.bCryptPasswordEncoder
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.invitationsRepository,
                this.usersRepository,
                this.bCryptPasswordEncoder
        );
    }

    @Test
    void register1Test() {
        // Arrange
        var requestUserRegistration1 = new RequestUserRegistration1(
                Username.random(),
                Password.random(),
                Password.random(),
                randomZoneId(),
                randomString()
        );
        var invitation = entity(Invitation.class);
        when(this.invitationsRepository.findByValueAsAny(requestUserRegistration1.invitation())).thenReturn(invitation);
        var hashPassword = randomString();
        when(this.bCryptPasswordEncoder.encode(requestUserRegistration1.password().value())).thenReturn(hashPassword);
        var invitationCodeAC1 = ArgumentCaptor.forClass(Invitation.class);
        var invitationCodeAC2 = ArgumentCaptor.forClass(Invitation.class);

        // Act
        this.componentUnderTest.register1(requestUserRegistration1);

        // Assert
        verify(this.invitationsRepository).findByValueAsAny(requestUserRegistration1.invitation());
        verify(this.bCryptPasswordEncoder).encode(requestUserRegistration1.password().value());
        verify(this.usersRepository).saveAs(eq(requestUserRegistration1), eq(Password.of(hashPassword)), invitationCodeAC1.capture());
        assertThat(invitationCodeAC1.getValue().invited()).isEqualTo(requestUserRegistration1.username());
        verify(this.invitationsRepository).saveAs(invitationCodeAC2.capture());
        assertThat(invitationCodeAC2.getValue().invited()).isEqualTo(requestUserRegistration1.username());
    }
}
