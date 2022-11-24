package io.tech1.framework.b2b.mongodb.security.jwt.validators.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbInvitationCode;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.InvitationCodeRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.tests.contexts.ValidatorsContext;
import io.tech1.framework.b2b.mongodb.security.jwt.validators.InvitationCodeRequestsValidator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Stream;

import static io.tech1.framework.domain.base.AbstractAuthority.*;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InvitationCodeRequestsValidatorImplTest {

    private static Stream<Arguments> validateCreateNewInvitationCodeTest() {
        return Stream.of(
                Arguments.of(new RequestNewInvitationCodeParams(Collections.emptySet()), invalidAttribute("authorities")),
                Arguments.of(new RequestNewInvitationCodeParams(new HashSet<>(asList(INVITATION_CODE_READ, "invitationCode:send"))), "Invitation code request params contains unsupported authority"),
                Arguments.of(new RequestNewInvitationCodeParams(new HashSet<>(asList(INVITATION_CODE_READ, SUPER_ADMIN))), "Invitation code request params contains unsupported authority"),
                Arguments.of(new RequestNewInvitationCodeParams(new HashSet<>(asList(INVITATION_CODE_READ, INVITATION_CODE_WRITE))), null)
        );
    }

    @Configuration
    @Import({
            ValidatorsContext.class
    })
    static class ContextConfiguration {

    }

    private final InvitationCodeRepository invitationCodeRepository;

    private final InvitationCodeRequestsValidator componentUnderTest;

    @ParameterizedTest
    @MethodSource("validateCreateNewInvitationCodeTest")
    public void validateCreateNewInvitationCodeTest(RequestNewInvitationCodeParams requestNewInvitationCodeParams, String exceptionMessage) {
        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateCreateNewInvitationCode(requestNewInvitationCodeParams));

        // Assert
        if (nonNull(exceptionMessage)) {
            assertThat(throwable).isNotNull();
            assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
            assertThat(throwable).hasMessageStartingWith(exceptionMessage);
        } else {
            assertThat(throwable).isNull();
        }
    }

    @Test
    public void validateDeleteByIdAccessDeniedTest() {
        // Arrange
        var currentUser = entity(DbUser.class);
        var invitationCodeId = randomString();
        var dbInvitationCode = entity(DbInvitationCode.class);
        when(this.invitationCodeRepository.requirePresence(eq(invitationCodeId))).thenReturn(dbInvitationCode);

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateDeleteById(currentUser, invitationCodeId));

        // Assert
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
        assertThat(throwable).hasMessageStartingWith("Access denied. Username: `" + currentUser.getUsername()+ "`, Entity: `InvitationCode`. Value: `" + invitationCodeId+ "`");
        verify(this.invitationCodeRepository).requirePresence(eq(invitationCodeId));
    }

    @Test
    public void validateDeleteByIdOkTest() {
        // Arrange
        var currentUser = entity(DbUser.class);
        var invitationCodeId = randomString();
        var dbInvitationCode = entity(DbInvitationCode.class);
        dbInvitationCode.setOwner(currentUser.getUsername());
        when(this.invitationCodeRepository.requirePresence(eq(invitationCodeId))).thenReturn(dbInvitationCode);

        // Act
        this.componentUnderTest.validateDeleteById(currentUser, invitationCodeId);

        // Assert
        verify(this.invitationCodeRepository).requirePresence(eq(invitationCodeId));
    }
}
