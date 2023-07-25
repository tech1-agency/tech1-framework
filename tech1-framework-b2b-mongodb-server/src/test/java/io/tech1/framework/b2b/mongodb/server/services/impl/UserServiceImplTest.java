package io.tech1.framework.b2b.mongodb.server.services.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.UserRepository;
import io.tech1.framework.b2b.mongodb.server.services.UserService;
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

import static io.tech1.framework.domain.utilities.random.EntityUtility.list345;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class UserServiceImplTest  {

    @Configuration
    static class ContextConfiguration {

        @Bean
        UserRepository userRepository() {
            return mock(UserRepository.class);
        }

        @Bean
        UserService userService() {
            return new UserServiceImpl(
                    this.userRepository()
            );
        }
    }

    private final UserRepository userRepository;

    private final UserService componentUnderTest;

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
    void findAll() {
        // Act
        var expected = list345(DbUser.class);
        when(this.userRepository.findAll()).thenReturn(expected);

        // Act
        var actual = this.componentUnderTest.findAll();

        // Assert
        verify(this.userRepository).findAll();
        assertThat(actual).isEqualTo(expected);
    }
}
