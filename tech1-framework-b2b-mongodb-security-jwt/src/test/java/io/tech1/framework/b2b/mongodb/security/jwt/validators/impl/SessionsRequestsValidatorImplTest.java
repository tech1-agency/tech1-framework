package io.tech1.framework.b2b.mongodb.security.jwt.validators.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUserSession;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.UserSessionRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.tests.contexts.TestsApplicationValidatorsContext;
import io.tech1.framework.b2b.mongodb.security.jwt.validators.SessionsRequestsValidator;
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
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class SessionsRequestsValidatorImplTest {

    @Configuration
    @Import({
            TestsApplicationValidatorsContext.class
    })
    static class ContextConfiguration {

    }

    private final UserSessionRepository userSessionRepository;

    private final SessionsRequestsValidator componentUnderTest;

    @Test
    void validateDeleteByIdAccessDeniedTest() {
        // Arrange
        var currentUser = entity(DbUser.class);
        var sessionId = randomString();
        var session = entity(DbUserSession.class);
        when(this.userSessionRepository.requirePresence(sessionId)).thenReturn(session);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateDeleteById(currentUser, sessionId));

        // Assert
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Access denied. Username: `" + currentUser.getUsername()+ "`, Entity: `Session`. Value: `" + sessionId + "`");
        verify(this.userSessionRepository).requirePresence(sessionId);
    }

    @Test
    void validateDeleteByIdOkTest() {
        // Arrange
        var currentUser = entity(DbUser.class);
        var sessionId = randomString();
        var session = entity(DbUserSession.class);
        session.setUsername(currentUser.getUsername());
        when(this.userSessionRepository.requirePresence(sessionId)).thenReturn(session);

        // Act
        this.componentUnderTest.validateDeleteById(currentUser, sessionId);

        // Assert
        verify(this.userSessionRepository).requirePresence(sessionId);
    }
}
