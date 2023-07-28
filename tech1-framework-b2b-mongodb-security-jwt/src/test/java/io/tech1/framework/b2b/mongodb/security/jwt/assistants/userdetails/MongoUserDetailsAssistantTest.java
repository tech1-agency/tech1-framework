package io.tech1.framework.b2b.mongodb.security.jwt.assistants.userdetails;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUserRepository;
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
class MongoUserDetailsAssistantTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        MongoUserRepository userRepository() {
            return mock(MongoUserRepository.class);
        }

        @Bean
        MongoUserDetailsAssistant jwtUserDetailsAssistant() {
            return new MongoUserDetailsAssistant(
                    this.userRepository()
            );
        }
    }

    private final MongoUserRepository mongoUserRepository;

    private final MongoUserDetailsAssistant componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.mongoUserRepository
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.mongoUserRepository
        );
    }

    @Test
    void loadUserByUsernameExceptionTest() {
        // Arrange
        var dbUser = entity(MongoDbUser.class);
        var username = dbUser.getUsername();
        when(this.mongoUserRepository.findByUsername(username)).thenReturn(null);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.loadUserByUsername(username.identifier()));

        // Assert
        verify(this.mongoUserRepository).findByUsername(username);
        assertThat(throwable)
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(entityNotFound("Username", username.identifier()));
    }

    @Test
    void loadUserByUsernameTest() {
        // Arrange
        var dbUser = entity(MongoDbUser.class);
        var username = dbUser.getUsername();
        when(this.mongoUserRepository.findByUsername(username)).thenReturn(dbUser);

        // Act
        var jwtUser = this.componentUnderTest.loadUserByUsername(username.identifier());

        // Assert
        verify(this.mongoUserRepository).findByUsername(username);
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
