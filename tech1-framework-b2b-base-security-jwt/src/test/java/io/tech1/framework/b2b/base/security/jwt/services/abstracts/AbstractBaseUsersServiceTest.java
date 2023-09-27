package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserChangePassword1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate2;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.repositories.UsersRepository;
import io.tech1.framework.domain.base.Email;
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
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomZoneId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AbstractBaseUsersServiceTest {
    @Configuration
    static class ContextConfiguration {
        @Bean
        UsersRepository userRepository() {
            return mock(UsersRepository.class);
        }

        @Bean
        BCryptPasswordEncoder bCryptPasswordEncoder() {
            return mock(BCryptPasswordEncoder.class);
        }

        @Bean
        AbstractBaseUsersService baseUserService() {
            return new AbstractBaseUsersService(
                    this.userRepository(),
                    this.bCryptPasswordEncoder()
            ) {};
        }
    }

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final AbstractBaseUsersService componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.usersRepository,
                this.bCryptPasswordEncoder
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.usersRepository,
                this.bCryptPasswordEncoder
        );
    }

    @Test
    void updateUser1Test() {
        // Arrange
        var requestUserUpdate1 = new RequestUserUpdate1(
                randomZoneId().getId(),
                Email.random(),
                randomString()
        );
        var user = entity(JwtUser.class);
        var userAC = ArgumentCaptor.forClass(JwtUser.class);

        // Act
        this.componentUnderTest.updateUser1(user, requestUserUpdate1);

        // Assert
        verify(this.usersRepository).saveAs(userAC.capture());
        assertThat(userAC.getValue().username()).isEqualTo(user.username());
        assertThat(userAC.getValue().zoneId()).isEqualTo(ZoneId.of(requestUserUpdate1.zoneId()));
        assertThat(userAC.getValue().name()).isEqualTo(requestUserUpdate1.name());
        assertThat(userAC.getValue().email()).isEqualTo(requestUserUpdate1.email());
        // WARNING: no verifications on static SecurityContextHolder
    }

    @Test
    void updateUser2Test() {
        // Arrange
        var requestUserUpdate2 = new RequestUserUpdate2(
                randomZoneId().getId(),
                randomString()
        );
        var user = entity(JwtUser.class);
        var userAC = ArgumentCaptor.forClass(JwtUser.class);

        // Act
        this.componentUnderTest.updateUser2(user, requestUserUpdate2);

        // Assert
        verify(this.usersRepository).saveAs(userAC.capture());
        assertThat(userAC.getValue().username()).isEqualTo(user.username());
        assertThat(userAC.getValue().zoneId()).isEqualTo(ZoneId.of(requestUserUpdate2.zoneId()));
        assertThat(userAC.getValue().name()).isEqualTo(requestUserUpdate2.name());
        // WARNING: no verifications on static SecurityContextHolder
    }

    @Test
    void changePassword1Test() {
        // Arrange
        var requestUserChangePassword1 = entity(RequestUserChangePassword1.class);
        var user = entity(JwtUser.class);
        var hashPassword = randomString();
        when(this.bCryptPasswordEncoder.encode(requestUserChangePassword1.newPassword().value())).thenReturn(hashPassword);
        var jwtUserAC = ArgumentCaptor.forClass(JwtUser.class);

        // Act
        this.componentUnderTest.changePassword1(user, requestUserChangePassword1);

        // Assert
        verify(this.bCryptPasswordEncoder).encode(requestUserChangePassword1.newPassword().value());
        verify(this.usersRepository).saveAs(jwtUserAC.capture());
        assertThat(jwtUserAC.getValue().username()).isEqualTo(user.username());
        assertThat(jwtUserAC.getValue().password().value()).isEqualTo(hashPassword);
        // WARNING: no verifications on static SecurityContextHolder
    }
}
