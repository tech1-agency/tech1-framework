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
        when(this.userRepository.findByUsername(eq(username))).thenReturn(null);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.loadUserByUsername(username.getIdentifier()));

        // Assert
        verify(this.userRepository).findByUsername(eq(username));
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(UsernameNotFoundException.class);
        assertThat(throwable).hasMessage(entityNotFound("Username", username.getIdentifier()));
    }

    @Test
    void loadUserByUsernameTest() {
        // Arrange
        var dbUser = entity(DbUser.class);
        var username = dbUser.getUsername();
        when(this.userRepository.findByUsername(eq(username))).thenReturn(dbUser);

        // Act
        var jwtUser = this.componentUnderTest.loadUserByUsername(username.getIdentifier());

        // Assert
        verify(this.userRepository).findByUsername(eq(username));
        assertThat(jwtUser).isNotNull();
        assertThat(jwtUser.getDbUser()).isEqualTo(dbUser);
        assertThat(jwtUser.getUsername()).isEqualTo(username.getIdentifier());
        assertThat(jwtUser.getPassword()).isEqualTo(dbUser.getPassword().getValue());
        assertThat(jwtUser.isAccountNonExpired()).isTrue();
        assertThat(jwtUser.isAccountNonLocked()).isTrue();
        assertThat(jwtUser.isCredentialsNonExpired()).isTrue();
        assertThat(jwtUser.isEnabled()).isTrue();
    }
}
