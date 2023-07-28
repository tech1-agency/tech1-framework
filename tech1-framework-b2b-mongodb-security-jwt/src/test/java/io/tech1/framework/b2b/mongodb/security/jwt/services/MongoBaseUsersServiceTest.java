package io.tech1.framework.b2b.mongodb.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserChangePassword1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate2;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersService;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUser;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUsersRepository;
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
class MongoBaseUsersServiceTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        MongoUsersRepository userRepository() {
            return mock(MongoUsersRepository.class);
        }

        @Bean
        BCryptPasswordEncoder bCryptPasswordEncoder() {
            return mock(BCryptPasswordEncoder.class);
        }

        @Bean
        BaseUsersService baseUserService() {
            return new MongoBaseUsersService(
                    this.userRepository(),
                    this.bCryptPasswordEncoder()
            );
        }
    }

    private final MongoUsersRepository mongoUsersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final BaseUsersService componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.mongoUsersRepository,
                this.bCryptPasswordEncoder
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.mongoUsersRepository,
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
        var jwtUser = entity(JwtUser.class);
        var dbUser = new MongoDbUser(jwtUser.username(), jwtUser.password(), jwtUser.zoneId().getId(), jwtUser.authorities());
        when(this.mongoUsersRepository.findByUsername(jwtUser.username())).thenReturn(dbUser);
        var dbUserAC = ArgumentCaptor.forClass(MongoDbUser.class);

        // Act
        this.componentUnderTest.updateUser1(jwtUser, requestUserUpdate1);

        // Assert
        verify(this.mongoUsersRepository).findByUsername(jwtUser.username());
        verify(this.mongoUsersRepository).save(dbUserAC.capture());
        assertThat(dbUserAC.getValue().getUsername()).isEqualTo(jwtUser.username());
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
        var jwtUser = entity(JwtUser.class);
        var dbUser = new MongoDbUser(jwtUser.username(), jwtUser.password(), jwtUser.zoneId().getId(), jwtUser.authorities());
        when(this.mongoUsersRepository.findByUsername(jwtUser.username())).thenReturn(dbUser);
        var dbUserAC = ArgumentCaptor.forClass(MongoDbUser.class);

        // Act
        this.componentUnderTest.updateUser2(jwtUser, requestUserUpdate2);

        // Assert
        verify(this.mongoUsersRepository).findByUsername(jwtUser.username());
        verify(this.mongoUsersRepository).save(dbUserAC.capture());
        assertThat(dbUserAC.getValue().getUsername()).isEqualTo(jwtUser.username());
        assertThat(dbUserAC.getValue().getZoneId()).isEqualTo(ZoneId.of(requestUserUpdate2.zoneId()));
        assertThat(dbUserAC.getValue().getName()).isEqualTo(requestUserUpdate2.name());
        // WARNING: no verifications on static SecurityContextHolder
    }

    @Test
    void changePassword1Test() {
        // Arrange
        var requestUserChangePassword1 = entity(RequestUserChangePassword1.class);
        var jwtUser = entity(JwtUser.class);
        var dbUser = new MongoDbUser(jwtUser.username(), jwtUser.password(), jwtUser.zoneId().getId(), jwtUser.authorities());
        when(this.mongoUsersRepository.findByUsername(jwtUser.username())).thenReturn(dbUser);
        var hashPassword = randomString();
        when(this.bCryptPasswordEncoder.encode(requestUserChangePassword1.newPassword().value())).thenReturn(hashPassword);
        ArgumentCaptor<MongoDbUser> dbUserAC = ArgumentCaptor.forClass(MongoDbUser.class);

        // Act
        this.componentUnderTest.changePassword1(jwtUser, requestUserChangePassword1);

        // Assert
        verify(this.mongoUsersRepository).findByUsername(jwtUser.username());
        verify(this.bCryptPasswordEncoder).encode(requestUserChangePassword1.newPassword().value());
        verify(this.mongoUsersRepository).save(dbUserAC.capture());
        assertThat(dbUserAC.getValue().getUsername()).isEqualTo(jwtUser.username());
        assertThat(dbUserAC.getValue().getPassword().value()).isEqualTo(hashPassword);
        // WARNING: no verifications on static SecurityContextHolder
    }
}