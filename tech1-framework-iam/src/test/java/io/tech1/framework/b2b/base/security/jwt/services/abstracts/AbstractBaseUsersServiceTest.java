package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserChangePasswordBasic;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate1;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserUpdate2;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
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

import static io.tech1.framework.foundation.domain.tests.constants.TestsJunitConstants.FIVE_TIMES;
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
            return new BCryptPasswordEncoder(11);
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
                this.usersRepository
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.usersRepository
        );
    }

    @Test
    void updateUser1() {
        // Arrange
        var request = RequestUserUpdate1.testsHardcoded();
        var user = JwtUser.testsHardcoded();
        var userAC = ArgumentCaptor.forClass(JwtUser.class);

        // Act
        this.componentUnderTest.updateUser1(user, request);

        // Assert
        verify(this.usersRepository).saveAs(userAC.capture());
        assertThat(userAC.getValue().username()).isEqualTo(user.username());
        assertThat(userAC.getValue().zoneId()).isEqualTo(request.zoneId());
        assertThat(userAC.getValue().name()).isEqualTo(request.name());
        assertThat(userAC.getValue().email()).isEqualTo(request.email());
        // no verifications on static SecurityContextHolder
    }

    @Test
    void updateUser2() {
        // Arrange
        var request = RequestUserUpdate2.testsHardcoded();
        var user = JwtUser.testsHardcoded();
        var userAC = ArgumentCaptor.forClass(JwtUser.class);

        // Act
        this.componentUnderTest.updateUser2(user, request);

        // Assert
        verify(this.usersRepository).saveAs(userAC.capture());
        assertThat(userAC.getValue().username()).isEqualTo(user.username());
        assertThat(userAC.getValue().zoneId()).isEqualTo(request.zoneId());
        assertThat(userAC.getValue().name()).isEqualTo(request.name());
        // no verifications on static SecurityContextHolder
    }

    @RepeatedTest(FIVE_TIMES)
    void changePasswordRequired() {
        // Arrange
        var request = RequestUserChangePasswordBasic.testsHardcoded();
        var user = JwtUser.random();
        var userAC = ArgumentCaptor.forClass(JwtUser.class);

        // Act
        this.componentUnderTest.changePasswordRequired(user, request);

        // Assert
        verify(this.usersRepository).saveAs(userAC.capture());
        assertThat(userAC.getValue().passwordChangeRequired()).isFalse();
        assertThat(userAC.getValue().username()).isEqualTo(user.username());
        assertThat(
                this.bCryptPasswordEncoder.matches(
                        request.newPassword().value(),
                        userAC.getValue().getPassword()
                )
        ).isTrue();
        // no verifications on static SecurityContextHolder
    }

    @RepeatedTest(FIVE_TIMES)
    void changePassword1() {
        // Arrange
        var request = RequestUserChangePasswordBasic.testsHardcoded();
        var user = JwtUser.testsHardcoded();
        var userAC = ArgumentCaptor.forClass(JwtUser.class);

        // Act
        this.componentUnderTest.changePassword1(user, request);

        // Assert
        verify(this.usersRepository).saveAs(userAC.capture());
        assertThat(userAC.getValue().passwordChangeRequired()).isEqualTo(user.passwordChangeRequired());
        assertThat(userAC.getValue().username()).isEqualTo(user.username());
        assertThat(
                this.bCryptPasswordEncoder.matches(
                        request.newPassword().value(),
                        userAC.getValue().getPassword()
                )
        ).isTrue();
        // no verifications on static SecurityContextHolder
    }
}
