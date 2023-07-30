package io.tech1.framework.b2b.base.security.jwt.validators.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbUserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbUsersSessionsRepository;
import io.tech1.framework.b2b.base.security.jwt.tests.contexts.TestsApplicationValidatorsContext;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseUsersSessionsRequestsValidator;
import io.tech1.framework.b2b.base.security.jwt.validators.abtracts.AbstractBaseUsersSessionsRequestsValidator;
import io.tech1.framework.domain.base.Username;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomUserRequestMetadata;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AbstractBaseUsersSessionsRequestsValidatorTest {

    @Configuration
    @Import({
            TestsApplicationValidatorsContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final AnyDbUsersSessionsRepository usersSessionsRepository;

        @Bean
        BaseUsersSessionsRequestsValidator baseUsersSessionsRequestsValidator() {
            return new AbstractBaseUsersSessionsRequestsValidator(
                    this.usersSessionsRepository
            ) {};
        }
    }

    private final AnyDbUsersSessionsRepository usersSessionsRepository;

    private final BaseUsersSessionsRequestsValidator componentUnderTest;

    @Test
    void validateDeleteByIdAccessDeniedTest() {
        // Arrange
        var username = entity(Username.class);
        var sessionId = entity(UserSessionId.class);
        var session = entity(AnyDbUserSession.class);
        when(this.usersSessionsRepository.requirePresence(sessionId)).thenReturn(session);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateDeleteById(username, sessionId));

        // Assert
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Access denied. Username: `" + username + "`, Entity: `Session`. Value: `" + sessionId + "`");
        verify(this.usersSessionsRepository).requirePresence(sessionId);
    }

    @Test
    void validateDeleteByIdOkTest() {
        // Arrange
        var username = entity(Username.class);
        var session = new AnyDbUserSession(
                entity(UserSessionId.class),
                username,
                randomUserRequestMetadata(),
                entity(JwtRefreshToken.class)
        );
        when(this.usersSessionsRepository.requirePresence(session.id())).thenReturn(session);

        // Act
        this.componentUnderTest.validateDeleteById(username, session.id());

        // Assert
        verify(this.usersSessionsRepository).requirePresence(session.id());
    }
}
