package io.tech1.framework.b2b.mongodb.security.jwt.configurations;

import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoInvitationCodesRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUsersRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUsersSessionsRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ApplicationMongoRepositoriesTest {

    @Configuration
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {

        @Bean
        MongoInvitationCodesRepository invitationCodeRepository() {
            return mock(MongoInvitationCodesRepository.class);
        }

        @Bean
        MongoUsersRepository usersRepository() {
            return mock(MongoUsersRepository.class);
        }

        @Bean
        MongoUsersSessionsRepository usersSessionsRepository() {
            return mock(MongoUsersSessionsRepository.class);
        }

        @Bean
        ApplicationMongoRepositories applicationMongoRepositories() {
            return new ApplicationMongoRepositories(
                    this.invitationCodeRepository(),
                    this.usersRepository(),
                    this.usersSessionsRepository()
            );
        }
    }

    private final ApplicationMongoRepositories componentUnderTest;

    @Test
    void beansTests() {
        // Act
        var methods = Stream.of(this.componentUnderTest.getClass().getMethods())
                .map(Method::getName)
                .collect(Collectors.toList());

        // Assert
        assertThat(methods)
                .hasSize(10)
                .contains("tech1MongoRepositories");
    }
}
