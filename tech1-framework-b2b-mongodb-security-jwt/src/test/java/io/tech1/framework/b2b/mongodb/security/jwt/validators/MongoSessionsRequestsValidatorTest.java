package io.tech1.framework.b2b.mongodb.security.jwt.validators;

import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.validators.SessionsRequestsValidator;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUserSession;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUserSessionsRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.tests.contexts.TestsApplicationValidatorsContext;
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
class MongoSessionsRequestsValidatorTest {

    @Configuration
    @Import({
            TestsApplicationValidatorsContext.class
    })
    static class ContextConfiguration {

    }

    private final MongoUserSessionsRepository mongoUserSessionsRepository;

    private final SessionsRequestsValidator componentUnderTest;

    @Test
    void validateDeleteByIdAccessDeniedTest() {
        // Arrange
        var username = entity(Username.class);
        var sessionId = entity(UserSessionId.class);
        var session = entity(MongoDbUserSession.class);
        when(this.mongoUserSessionsRepository.requirePresence(sessionId)).thenReturn(session);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateDeleteById(username, sessionId));

        // Assert
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Access denied. Username: `" + username + "`, Entity: `Session`. Value: `" + sessionId + "`");
        verify(this.mongoUserSessionsRepository).requirePresence(sessionId);
    }

    @Test
    void validateDeleteByIdOkTest() {
        // Arrange
        var username = entity(Username.class);
        var sessionId = entity(UserSessionId.class);
        var session = entity(MongoDbUserSession.class);
        session.setUsername(username);
        when(this.mongoUserSessionsRepository.requirePresence(sessionId)).thenReturn(session);

        // Act
        this.componentUnderTest.validateDeleteById(username, sessionId);

        // Assert
        verify(this.mongoUserSessionsRepository).requirePresence(sessionId);
    }
}
