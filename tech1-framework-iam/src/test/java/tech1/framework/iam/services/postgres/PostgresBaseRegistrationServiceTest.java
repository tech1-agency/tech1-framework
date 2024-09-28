package tech1.framework.iam.services.postgres;

import tech1.framework.iam.repositories.postgres.PostgresInvitationCodesRepository;
import tech1.framework.iam.repositories.postgres.PostgresUsersRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class PostgresBaseRegistrationServiceTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        PostgresInvitationCodesRepository invitationCodeRepository() {
            return mock(PostgresInvitationCodesRepository.class);
        }

        @Bean
        PostgresUsersRepository userRepository() {
            return mock(PostgresUsersRepository.class);
        }

        @Bean
        BCryptPasswordEncoder bCryptPasswordEncoder() {
            return mock(BCryptPasswordEncoder.class);
        }

        @Bean
        PostgresBaseRegistrationService registrationService() {
            return new PostgresBaseRegistrationService(
                    this.invitationCodeRepository(),
                    this.userRepository(),
                    this.bCryptPasswordEncoder()
            ) {};
        }
    }

    private final PostgresInvitationCodesRepository invitationCodesRepository;
    private final PostgresUsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final PostgresBaseRegistrationService componentUnderTest;

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
    void verifyTransactionalAnnotationTest() {
        // Assert
        Arrays.stream(this.componentUnderTest.getClass().getMethods())
                .filter(method -> method.getName().contains("register1"))
                .forEach(method -> assertThat(method.isAnnotationPresent(Transactional.class)).isTrue());
    }
}
