package io.tech1.framework.b2b.mongodb.security.jwt.services.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbInvitationCode;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserRegistration1;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.InvitationCodeRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.UserRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.services.RegistrationService;
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
public class RegistrationServiceImplTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        InvitationCodeRepository invitationCodeRepository() {
            return mock(InvitationCodeRepository.class);
        }

        @Bean
        UserRepository userRepository() {
            return mock(UserRepository.class);
        }

        @Bean
        BCryptPasswordEncoder bCryptPasswordEncoder() {
            return mock(BCryptPasswordEncoder.class);
        }

        @Bean
        RegistrationService registrationService() {
            return new RegistrationServiceImpl(
                    this.invitationCodeRepository(),
                    this.userRepository(),
                    this.bCryptPasswordEncoder()
            );
        }
    }

    private final InvitationCodeRepository invitationCodeRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final RegistrationService componentUnderTest;

    @BeforeEach
    public void beforeEach() {
        reset(
                this.invitationCodeRepository,
                this.userRepository,
                this.bCryptPasswordEncoder
        );
    }

    @AfterEach
    public void after() {
        verifyNoMoreInteractions(
                this.invitationCodeRepository,
                this.userRepository,
                this.bCryptPasswordEncoder
        );
    }

    @Test
    public void register1Test() {
        // Arrange
        var requestUserRegistration1 = new RequestUserRegistration1(
                randomUsername().getIdentifier(),
                randomPassword().getValue(),
                randomPassword().getValue(),
                randomZoneId().getId(),
                randomString()
        );
        var dbInvitationCode = entity(DbInvitationCode.class);
        when(this.invitationCodeRepository.findByValue(eq(requestUserRegistration1.getInvitationCode()))).thenReturn(dbInvitationCode);
        var hashPassword = randomString();
        when(this.bCryptPasswordEncoder.encode(eq(requestUserRegistration1.getPassword()))).thenReturn(hashPassword);
        var dbUserAC = ArgumentCaptor.forClass(DbUser.class);
        var savedDbUser = entity(DbUser.class);
        when(this.userRepository.save(any())).thenReturn(savedDbUser);
        var dbInvitationCodeAC = ArgumentCaptor.forClass(DbInvitationCode.class);

        // Act
        this.componentUnderTest.register1(requestUserRegistration1);

        // Assert
        verify(this.invitationCodeRepository).findByValue(eq(requestUserRegistration1.getInvitationCode()));
        verify(this.bCryptPasswordEncoder).encode(eq(requestUserRegistration1.getPassword()));
        verify(this.userRepository).save(dbUserAC.capture());
        assertThat(dbUserAC.getValue().getUsername().getIdentifier()).isEqualTo(requestUserRegistration1.getUsername());
        assertThat(dbUserAC.getValue().getPassword()).isEqualTo(hashPassword);
        assertThat(dbUserAC.getValue().getAuthorities()).isEqualTo(dbInvitationCode.getAuthorities());
        verify(this.invitationCodeRepository).save(dbInvitationCodeAC.capture());
        assertThat(dbInvitationCodeAC.getValue().getInvited()).isEqualTo(savedDbUser.getUsername());
    }

}
