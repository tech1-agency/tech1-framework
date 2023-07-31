package io.tech1.framework.b2b.postgres.server.services.impl;

import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUser;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresUsersRepository;
import io.tech1.framework.b2b.postgres.server.services.UsersService;
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
class UsersServiceImplTest {

    @Configuration
    static class ContextConfiguration {

        @Bean
        PostgresUsersRepository usersRepository() {
            return mock(PostgresUsersRepository.class);
        }

        @Bean
        UsersService userService() {
            return new UsersServiceImpl(
                    this.usersRepository()
            );
        }
    }

    private final PostgresUsersRepository postgresUsersRepository;

    private final UsersService componentUnderTest;

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
    void findAll() {
        // Act
        var expected = list345(PostgresDbUser.class);
        when(this.postgresUsersRepository.findAll()).thenReturn(expected);

        // Act
        var actual = this.componentUnderTest.findAll();

        // Assert
        verify(this.postgresUsersRepository).findAll();
        assertThat(actual).isEqualTo(expected);
    }
}
