package io.tech1.framework.b2b.postgres.security.jwt.validators;

import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseUsersSessionsRequestsValidator;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUserSession;
import io.tech1.framework.b2b.postgres.security.jwt.repositories.PostgresUsersSessionsRepository;
import io.tech1.framework.b2b.postgres.security.jwt.tests.contexts.PostgresTestsApplicationValidatorsContext;
import io.tech1.framework.domain.base.Username;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class PostgresBaseUsersSessionsRequestsValidatorTest {

    @Configuration
    @Import({
            PostgresTestsApplicationValidatorsContext.class
    })
    static class ContextConfiguration {

    }

    private final PostgresUsersSessionsRepository postgresUsersSessionsRepository;

    private final BaseUsersSessionsRequestsValidator componentUnderTest;

    @Test
    void validateDeleteByIdAccessDeniedTest() {
        // Arrange
        var username = entity(Username.class);
        var sessionId = entity(UserSessionId.class);
        var session = entity(PostgresDbUserSession.class);
        when(this.postgresUsersSessionsRepository.requirePresence(sessionId)).thenReturn(session);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateDeleteById(username, sessionId));

        // Assert
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Access denied. Username: `" + username + "`, Entity: `Session`. Value: `" + sessionId + "`");
        verify(this.postgresUsersSessionsRepository).requirePresence(sessionId);
    }

    @Test
    void validateDeleteByIdOkTest() {
        // Arrange
        var username = entity(Username.class);
        var sessionId = entity(UserSessionId.class);
        var session = entity(PostgresDbUserSession.class);
        session.setUsername(username);
        when(this.postgresUsersSessionsRepository.requirePresence(sessionId)).thenReturn(session);

        // Act
        this.componentUnderTest.validateDeleteById(username, sessionId);

        // Assert
        verify(this.postgresUsersSessionsRepository).requirePresence(sessionId);
    }
}
