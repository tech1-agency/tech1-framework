package tech1.framework.iam.validators.abstracts;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.properties.ApplicationFrameworkProperties;
import tech1.framework.foundation.domain.tuples.TuplePresence;
import tech1.framework.iam.domain.db.InvitationCode;
import tech1.framework.iam.domain.dto.requests.RequestNewInvitationCodeParams;
import tech1.framework.iam.domain.identifiers.InvitationCodeId;
import tech1.framework.iam.repositories.InvitationCodesRepository;
import tech1.framework.iam.tests.contexts.TestsApplicationValidatorsContext;
import tech1.framework.iam.validators.BaseInvitationCodesRequestsValidator;
import tech1.framework.iam.validators.abtracts.AbstractBaseInvitationCodesRequestsValidator;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tech1.framework.foundation.domain.base.AbstractAuthority.*;
import static tech1.framework.foundation.utilities.exceptions.ExceptionsMessagesUtility.entityAccessDenied;
import static tech1.framework.foundation.utilities.exceptions.ExceptionsMessagesUtility.entityNotFound;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AbstractBaseInvitationCodesRequestsValidatorTest {

    private static Stream<Arguments> validateCreateNewInvitationCodeTest() {
        return Stream.of(
                Arguments.of(new RequestNewInvitationCodeParams(Set.of(INVITATION_CODE_READ, "invitationCode:send")), "Authorities must contains: [admin, invitationCode:read, invitationCode:write, user]"),
                Arguments.of(new RequestNewInvitationCodeParams(Set.of(INVITATION_CODE_READ, SUPERADMIN)), "Authorities must contains: [admin, invitationCode:read, invitationCode:write, user]"),
                Arguments.of(new RequestNewInvitationCodeParams(Set.of()), null),
                Arguments.of(new RequestNewInvitationCodeParams(Set.of(INVITATION_CODE_READ, INVITATION_CODE_WRITE)), null)
        );
    }

    @Configuration
    @Import({
            TestsApplicationValidatorsContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final InvitationCodesRepository invitationCodesRepository;
        private final ApplicationFrameworkProperties applicationFrameworkProperties;

        @Bean
        BaseInvitationCodesRequestsValidator baseInvitationCodesRequestsValidator() {
            return new AbstractBaseInvitationCodesRequestsValidator(
                    this.invitationCodesRepository,
                    this.applicationFrameworkProperties
            ) {};
        }
    }

    private final InvitationCodesRepository invitationCodesRepository;

    private final BaseInvitationCodesRequestsValidator componentUnderTest;

    @ParameterizedTest
    @MethodSource("validateCreateNewInvitationCodeTest")
    void validateCreateNewInvitationCodeTest(RequestNewInvitationCodeParams request, String exceptionMessage) {
        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateCreateNewInvitationCode(request));

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
    void validateDeleteByIdNotFoundTest() {
        // Arrange
        var username = Username.random();
        var invitationCodeId = InvitationCodeId.random();
        when(this.invitationCodesRepository.isPresent(invitationCodeId)).thenReturn(TuplePresence.absent());

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateDeleteById(username, invitationCodeId));

        // Assert
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(entityNotFound("Invitation code", invitationCodeId.value()));
        verify(this.invitationCodesRepository).isPresent(invitationCodeId);
    }

    @Test
    void validateDeleteByIdAccessDeniedTest() {
        // Arrange
        var username = Username.random();
        var invitationCodeId = InvitationCodeId.random();
        var invitationCode = InvitationCode.random();
        when(this.invitationCodesRepository.isPresent(invitationCodeId)).thenReturn(TuplePresence.present(invitationCode));

        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateDeleteById(username, invitationCodeId));

        // Assert
        assertThat(throwable)
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage(entityAccessDenied("Invitation code", invitationCodeId.value()));
        verify(this.invitationCodesRepository).isPresent(invitationCodeId);
    }

    @Test
    void validateDeleteByIdOkTest() {
        // Arrange
        var invitationCodeId = InvitationCodeId.random();
        var invitationCode = InvitationCode.random();
        when(this.invitationCodesRepository.isPresent(invitationCodeId)).thenReturn(TuplePresence.present(invitationCode));

        // Act
        this.componentUnderTest.validateDeleteById(invitationCode.owner(), invitationCodeId);

        // Assert
        verify(this.invitationCodesRepository).isPresent(invitationCodeId);
    }
}
