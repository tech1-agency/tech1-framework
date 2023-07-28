package io.tech1.framework.b2b.mongodb.security.jwt.assistants.userdetails;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.UserRepository;
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
class JwtUserDetailsAssistantTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        UserRepository userRepository() {
            return mock(UserRepository.class);
        }

        @Bean
        JwtUserDetailsAssistant jwtUserDetailsAssistant() {
            return new JwtUserDetailsAssistant(
                    this.userRepository()
            );
        }
    }

    private final UserRepository userRepository;

    private final JwtUserDetailsAssistant componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.userRepository
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.userRepository
        );
    }

    @Test
    void loadUserByUsernameExceptionTest() {
        // Arrange
        var dbUser = entity(DbUser.class);
        var username = dbUser.getUsername();
        when(this.userRepository.findByUsername(username)).thenReturn(null);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.loadUserByUsername(username.identifier()));

        // Assert
        verify(this.userRepository).findByUsername(username);
        assertThat(throwable)
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(entityNotFound("Username", username.identifier()));
    }

    @Test
    void loadUserByUsernameTest() {
        // Arrange
        var dbUser = entity(DbUser.class);
        var username = dbUser.getUsername();
        when(this.userRepository.findByUsername(username)).thenReturn(dbUser);

        // Act
        var jwtUser = this.componentUnderTest.loadUserByUsername(username.identifier());

        // Assert
        verify(this.userRepository).findByUsername(username);
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
