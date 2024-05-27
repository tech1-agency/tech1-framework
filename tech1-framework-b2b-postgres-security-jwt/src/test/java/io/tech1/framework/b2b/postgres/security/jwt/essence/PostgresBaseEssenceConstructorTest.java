package io.tech1.framework.b2b.postgres.security.jwt.essence;

import io.tech1.framework.b2b.base.security.jwt.essense.EssenceConstructor;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresInvitationCodesRepository;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresUsersRepository;
import io.tech1.framework.domain.properties.base.DefaultUser;
import io.tech1.framework.domain.properties.ApplicationFrameworkProperties;
import io.tech1.framework.domain.properties.ApplicationFrameworkPropertiesTestsHardcodedContext;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;

import static io.tech1.framework.domain.utilities.random.EntityUtility.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class PostgresBaseEssenceConstructorTest {

    @Configuration
    @Import({
            ApplicationFrameworkPropertiesTestsHardcodedContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final ApplicationFrameworkProperties applicationFrameworkProperties;

        @Bean
        PostgresInvitationCodesRepository invitationCodeRepository() {
            return mock(PostgresInvitationCodesRepository.class);
        }

        @Bean
        PostgresUsersRepository userRepository() {
            return mock(PostgresUsersRepository.class);
        }

        @Bean
        EssenceConstructor essenceConstructor() {
            return new PostgresBaseEssenceConstructor(
                    this.invitationCodeRepository(),
                    this.userRepository(),
                    this.applicationFrameworkProperties
            );
        }
    }

    private final PostgresInvitationCodesRepository invitationCodesRepository;
    private final PostgresUsersRepository usersRepository;

    private final EssenceConstructor componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.invitationCodesRepository,
                this.usersRepository
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.invitationCodesRepository,
                this.usersRepository
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    void saveDefaultUsersTest() {
        // Arrange
        var defaultUsers = list345(DefaultUser.class);

        // Act
        var actual = this.componentUnderTest.saveDefaultUsers(defaultUsers);

        // Assert
        var userAC = ArgumentCaptor.forClass(List.class);
        verify(this.usersRepository).saveAll(userAC.capture());
        assertThat(actual)
                .isEqualTo(defaultUsers.size())
                .isEqualTo(userAC.getValue().size());
    }

    @SuppressWarnings("unchecked")
    @Test
    void saveInvitationCodesTest() {
        // Arrange
        var defaultUser = entity(DefaultUser.class);
        var authorities = set345(SimpleGrantedAuthority.class);

        // Act
        this.componentUnderTest.saveInvitationCodes(defaultUser, authorities);

        // Assert
        var userAC = ArgumentCaptor.forClass(List.class);
        verify(this.invitationCodesRepository).saveAll(userAC.capture());
        assertThat(userAC.getValue()).hasSize(10);
    }
}
