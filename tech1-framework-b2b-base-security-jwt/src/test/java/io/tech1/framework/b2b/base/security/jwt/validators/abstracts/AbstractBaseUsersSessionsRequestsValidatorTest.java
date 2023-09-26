package io.tech1.framework.b2b.base.security.jwt.validators.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.db.UserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.repositories.UsersSessionsRepository;
import io.tech1.framework.b2b.base.security.jwt.tests.contexts.TestsApplicationValidatorsContext;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseUsersSessionsRequestsValidator;
import io.tech1.framework.b2b.base.security.jwt.validators.abtracts.AbstractBaseUsersSessionsRequestsValidator;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.tuples.TuplePresence;
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
        private final UsersSessionsRepository usersSessionsRepository;

        @Bean
        BaseUsersSessionsRequestsValidator baseUsersSessionsRequestsValidator() {
            return new AbstractBaseUsersSessionsRequestsValidator(
                    this.usersSessionsRepository
            ) {};
        }
    }

    private final UsersSessionsRepository usersSessionsRepository;

    private final BaseUsersSessionsRequestsValidator componentUnderTest;

    @Test
    void validateDeleteByIdNotFoundTest() {
        // Arrange
        var username = entity(Username.class);
        var sessionId = entity(UserSessionId.class);
        when(this.usersSessionsRepository.isPresent(sessionId)).thenReturn(TuplePresence.absent());

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateDeleteById(username, sessionId));

        // Assert
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Session: Not Found, id = " + sessionId);
        verify(this.usersSessionsRepository).isPresent(sessionId);
    }

    @Test
    void validateDeleteByIdAccessDeniedTest() {
        // Arrange
        var username = entity(Username.class);
        var sessionId = entity(UserSessionId.class);
        var session = entity(UserSession.class);
        when(this.usersSessionsRepository.isPresent(sessionId)).thenReturn(TuplePresence.present(session));

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateDeleteById(username, sessionId));

        // Assert
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("Session: Access Denied, id = " + sessionId);
        verify(this.usersSessionsRepository).isPresent(sessionId);
    }

    @Test
    void validateDeleteByIdOkTest() {
        // Arrange
        var session = entity(UserSession.class);
        when(this.usersSessionsRepository.isPresent(session.id())).thenReturn(TuplePresence.present(session));

        // Act
        this.componentUnderTest.validateDeleteById(session.username(), session.id());

        // Assert
        verify(this.usersSessionsRepository).isPresent(session.id());
    }
}
