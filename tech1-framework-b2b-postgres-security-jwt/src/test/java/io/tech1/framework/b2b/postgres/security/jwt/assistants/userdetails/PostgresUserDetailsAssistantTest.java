package io.tech1.framework.b2b.postgres.security.jwt.assistants.userdetails;

import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUser;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresUsersRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.entityNotFound;
import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class PostgresUserDetailsAssistantTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        PostgresUsersRepository userRepository() {
            return mock(PostgresUsersRepository.class);
        }

        @Bean
        PostgresUserDetailsAssistant jwtUserDetailsAssistant() {
            return new PostgresUserDetailsAssistant(
                    this.userRepository()
            );
        }
    }

    private final PostgresUsersRepository postgresUsersRepository;

    private final PostgresUserDetailsAssistant componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.postgresUsersRepository
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.postgresUsersRepository
        );
    }

    @Test
    void loadUserByUsernameExceptionTest() {
        // Arrange
        var dbUser = entity(PostgresDbUser.class);
        var username = dbUser.getUsername();
        when(this.postgresUsersRepository.findByUsername(username)).thenReturn(null);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.loadUserByUsername(username.identifier()));

        // Assert
        verify(this.postgresUsersRepository).findByUsername(username);
        assertThat(throwable)
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(entityNotFound("Username", username.identifier()));
    }

    @Test
    void loadUserByUsernameTest() {
        // Arrange
        var dbUser = entity(PostgresDbUser.class);
        var username = dbUser.getUsername();
        when(this.postgresUsersRepository.findByUsername(username)).thenReturn(dbUser);

        // Act
        var jwtUser = this.componentUnderTest.loadUserByUsername(username.identifier());

        // Assert
        verify(this.postgresUsersRepository).findByUsername(username);
        assertThat(jwtUser).isNotNull();
        assertThat(jwtUser.getUsername()).isEqualTo(username.identifier());
        assertThat(jwtUser.getPassword()).isEqualTo(dbUser.getPassword().value());
        assertThat(jwtUser.isAccountNonExpired()).isTrue();
        assertThat(jwtUser.isAccountNonLocked()).isTrue();
        assertThat(jwtUser.isCredentialsNonExpired()).isTrue();
        assertThat(jwtUser.isEnabled()).isTrue();
        assertThat(jwtUser.username()).isEqualTo(dbUser.getUsername());
        assertThat(jwtUser.password()).isEqualTo(dbUser.getPassword());
        assertThat(jwtUser.authorities()).isEqualTo(dbUser.getAuthorities());
        assertThat(jwtUser.authorities()).isEqualTo(dbUser.getAuthorities());
        assertThat(jwtUser.email()).isEqualTo(dbUser.getEmail());
        assertThat(jwtUser.name()).isEqualTo(dbUser.getName());
        assertThat(jwtUser.zoneId()).isEqualTo(dbUser.getZoneId());
        assertThat(jwtUser.attributes()).isEqualTo(dbUser.getAttributes());
    }
}
