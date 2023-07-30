package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserRegistration1;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbInvitationCodesRepository;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbUsersRepository;
import io.tech1.framework.b2b.base.security.jwt.services.BaseRegistrationService;
import io.tech1.framework.domain.base.Password;
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

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AbstractBaseRegistrationServiceTest {
    @Configuration
    static class ContextConfiguration {
        @Bean
        AnyDbInvitationCodesRepository invitationCodeRepository() {
            return mock(AnyDbInvitationCodesRepository.class);
        }

        @Bean
        AnyDbUsersRepository userRepository() {
            return mock(AnyDbUsersRepository.class);
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

    private final AnyDbInvitationCodesRepository invitationCodesRepository;
    private final AnyDbUsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final BaseRegistrationService componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.invitationCodesRepository,
                this.usersRepository,
                this.bCryptPasswordEncoder
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.invitationCodesRepository,
                this.usersRepository,
                this.bCryptPasswordEncoder
        );
    }

    @Test
    void register1Test() {
        // Arrange
        var requestUserRegistration1 = new RequestUserRegistration1(
                randomUsername(),
                randomPassword(),
                randomPassword(),
                randomZoneId().getId(),
                randomString()
        );
        var invitationCode = entity(AnyDbInvitationCode.class);
        when(this.invitationCodesRepository.findByValueAsAny(requestUserRegistration1.invitationCode())).thenReturn(invitationCode);
        var hashPassword = randomString();
        when(this.bCryptPasswordEncoder.encode(requestUserRegistration1.password().value())).thenReturn(hashPassword);
        var invitationCodeAC1 = ArgumentCaptor.forClass(AnyDbInvitationCode.class);
        var invitationCodeAC2 = ArgumentCaptor.forClass(AnyDbInvitationCode.class);

        // Act
        this.componentUnderTest.register1(requestUserRegistration1);

        // Assert
        verify(this.invitationCodesRepository).findByValueAsAny(requestUserRegistration1.invitationCode());
        verify(this.bCryptPasswordEncoder).encode(requestUserRegistration1.password().value());
        verify(this.usersRepository).saveAs(eq(requestUserRegistration1), eq(Password.of(hashPassword)), invitationCodeAC1.capture());
        assertThat(invitationCodeAC1.getValue().invited()).isEqualTo(requestUserRegistration1.username());
        verify(this.invitationCodesRepository).saveAs(invitationCodeAC2.capture());
        assertThat(invitationCodeAC2.getValue().invited()).isEqualTo(requestUserRegistration1.username());
    }
}
