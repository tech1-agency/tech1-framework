package io.tech1.framework.b2b.mongodb.server.services.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUsersRepository;
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
        MongoUsersRepository userRepository() {
            return mock(MongoUsersRepository.class);
        }

        @Bean
        UserService userService() {
            return new UserServiceImpl(
                    this.userRepository()
            );
        }
    }

    private final MongoUsersRepository mongoUsersRepository;

    private final UserService componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.mongoUsersRepository
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.mongoUsersRepository
        );
    }

    @Test
    void findAll() {
        // Act
        var expected = list345(MongoDbUser.class);
        when(this.mongoUsersRepository.findAll()).thenReturn(expected);

        // Act
        var actual = this.componentUnderTest.findAll();

        // Assert
        verify(this.mongoUsersRepository).findAll();
        assertThat(actual).isEqualTo(expected);
    }
}
