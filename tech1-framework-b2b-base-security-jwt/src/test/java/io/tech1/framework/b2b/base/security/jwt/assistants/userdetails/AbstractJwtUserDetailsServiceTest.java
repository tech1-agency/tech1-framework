package io.tech1.framework.b2b.base.security.jwt.assistants.userdetails;

import io.tech1.framework.b2b.base.security.jwt.repositories.UsersRepository;
import io.tech1.framework.domain.base.Username;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AbstractJwtUserDetailsServiceTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        UsersRepository usersRepository() {
            return mock(UsersRepository.class);
        }

        @Bean
        AbstractJwtUserDetailsService jwtUserDetailsService() {
            return new AbstractJwtUserDetailsService(
                    this.usersRepository()
            ) {};
        }
    }

    private final UsersRepository usersRepository;

    private final AbstractJwtUserDetailsService jwtUserDetailsService;

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
    void getCurrentUsernameTest() {
        // Arrange
        var username = Username.random();

        // Act
        this.jwtUserDetailsService.loadUserByUsername(username.identifier());

        // Assert
        verify(this.usersRepository).loadUserByUsername(username);
    }
}
