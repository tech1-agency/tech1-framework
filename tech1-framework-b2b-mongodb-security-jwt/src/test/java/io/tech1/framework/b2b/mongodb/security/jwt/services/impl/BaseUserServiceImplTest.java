package io.tech1.framework.b2b.mongodb.security.jwt.services.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.assistants.core.CurrentSessionAssistant;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserChangePassword1;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserUpdate2;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.UserRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.services.BaseUserService;
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

import java.time.ZoneId;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BaseUserServiceImplTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        CurrentSessionAssistant currentSessionAssistant() {
            return mock(CurrentSessionAssistant.class);
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
        BaseUserService baseUserService() {
            return new BaseUserServiceImpl(
                    this.currentSessionAssistant(),
                    this.userRepository(),
                    this.bCryptPasswordEncoder()
            );
        }
    }

    private final CurrentSessionAssistant currentSessionAssistant;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final BaseUserService componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.currentSessionAssistant,
                this.userRepository,
                this.bCryptPasswordEncoder
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.currentSessionAssistant,
                this.userRepository,
                this.bCryptPasswordEncoder
        );
    }

    @Test
    void updateUser1Test() {
        // Arrange
        var requestUserUpdate1 = new RequestUserUpdate1(
                randomZoneId().getId(),
                randomEmail(),
                randomString()
        );
        var currentJwtUser = entity(JwtUser.class);
        when(this.currentSessionAssistant.getCurrentJwtUser()).thenReturn(currentJwtUser);
        var dbUserAC = ArgumentCaptor.forClass(DbUser.class);

        // Act
        this.componentUnderTest.updateUser1(requestUserUpdate1);

        // Assert
        verify(this.currentSessionAssistant).getCurrentJwtUser();
        verify(this.userRepository).save(dbUserAC.capture());
        assertThat(dbUserAC.getValue().getUsername()).isEqualTo(currentJwtUser.dbUser().getUsername());
        assertThat(dbUserAC.getValue().getZoneId()).isEqualTo(ZoneId.of(requestUserUpdate1.zoneId()));
        assertThat(dbUserAC.getValue().getName()).isEqualTo(requestUserUpdate1.name());
        assertThat(dbUserAC.getValue().getEmail()).isEqualTo(requestUserUpdate1.email());
        // WARNING: no verifications on static SecurityContextHolder
    }

    @Test
    void updateUser2Test() {
        // Arrange
        var requestUserUpdate2 = new RequestUserUpdate2(
                randomZoneId().getId(),
                randomString()
        );
        var currentJwtUser = entity(JwtUser.class);
        when(this.currentSessionAssistant.getCurrentJwtUser()).thenReturn(currentJwtUser);
        var dbUserAC = ArgumentCaptor.forClass(DbUser.class);

        // Act
        this.componentUnderTest.updateUser2(requestUserUpdate2);

        // Assert
        verify(this.currentSessionAssistant).getCurrentJwtUser();
        verify(this.userRepository).save(dbUserAC.capture());
        assertThat(dbUserAC.getValue().getUsername()).isEqualTo(currentJwtUser.dbUser().getUsername());
        assertThat(dbUserAC.getValue().getZoneId()).isEqualTo(ZoneId.of(requestUserUpdate2.zoneId()));
        assertThat(dbUserAC.getValue().getName()).isEqualTo(requestUserUpdate2.name());
        assertThat(dbUserAC.getValue().getEmail()).isEqualTo(currentJwtUser.dbUser().getEmail());
        // WARNING: no verifications on static SecurityContextHolder
    }

    @Test
    void changePassword1Tes() {
        // Arrange
        var requestUserChangePassword1 = entity(RequestUserChangePassword1.class);
        var currentJwtUser = entity(JwtUser.class);
        when(this.currentSessionAssistant.getCurrentJwtUser()).thenReturn(currentJwtUser);
        String hashPassword = randomString();
        when(this.bCryptPasswordEncoder.encode(requestUserChangePassword1.newPassword().value())).thenReturn(hashPassword);
        ArgumentCaptor<DbUser> dbUserAC = ArgumentCaptor.forClass(DbUser.class);

        // Act
        this.componentUnderTest.changePassword1(requestUserChangePassword1);

        // Assert
        verify(this.currentSessionAssistant).getCurrentJwtUser();
        verify(this.bCryptPasswordEncoder).encode(requestUserChangePassword1.newPassword().value());
        verify(this.userRepository).save(dbUserAC.capture());
        assertThat(dbUserAC.getValue().getUsername()).isEqualTo(currentJwtUser.dbUser().getUsername());
        assertThat(dbUserAC.getValue().getPassword().value()).isEqualTo(hashPassword);
        // WARNING: no verifications on static SecurityContextHolder
    }
}
