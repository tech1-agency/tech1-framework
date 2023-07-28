package io.tech1.framework.b2b.mongodb.security.jwt.services.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbInvitationCode;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUser;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserRegistration1;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoInvitationCodesRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUsersRepository;
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
class RegistrationServiceImplTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        MongoInvitationCodesRepository invitationCodeRepository() {
            return mock(MongoInvitationCodesRepository.class);
        }

        @Bean
        MongoUsersRepository userRepository() {
            return mock(MongoUsersRepository.class);
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

    private final MongoInvitationCodesRepository mongoInvitationCodesRepository;
    private final MongoUsersRepository mongoUsersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final RegistrationService componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.mongoInvitationCodesRepository,
                this.mongoUsersRepository,
                this.bCryptPasswordEncoder
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.mongoInvitationCodesRepository,
                this.mongoUsersRepository,
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
        var dbInvitationCode = entity(MongoDbInvitationCode.class);
        when(this.mongoInvitationCodesRepository.findByValue(requestUserRegistration1.invitationCode())).thenReturn(dbInvitationCode);
        var hashPassword = randomString();
        when(this.bCryptPasswordEncoder.encode(requestUserRegistration1.password().value())).thenReturn(hashPassword);
        var dbUserAC = ArgumentCaptor.forClass(MongoDbUser.class);
        var savedDbUser = entity(MongoDbUser.class);
        when(this.mongoUsersRepository.save(any())).thenReturn(savedDbUser);
        var dbInvitationCodeAC = ArgumentCaptor.forClass(MongoDbInvitationCode.class);

        // Act
        this.componentUnderTest.register1(requestUserRegistration1);

        // Assert
        verify(this.mongoInvitationCodesRepository).findByValue(requestUserRegistration1.invitationCode());
        verify(this.bCryptPasswordEncoder).encode(requestUserRegistration1.password().value());
        verify(this.mongoUsersRepository).save(dbUserAC.capture());
        assertThat(dbUserAC.getValue().getUsername()).isEqualTo(requestUserRegistration1.username());
        assertThat(dbUserAC.getValue().getPassword().value()).isEqualTo(hashPassword);
        assertThat(dbUserAC.getValue().getAuthorities()).isEqualTo(dbInvitationCode.getAuthorities());
        verify(this.mongoInvitationCodesRepository).save(dbInvitationCodeAC.capture());
        assertThat(dbInvitationCodeAC.getValue().getInvited()).isEqualTo(savedDbUser.getUsername());
    }

}
