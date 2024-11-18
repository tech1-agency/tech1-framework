package jbst.iam.validators.abstracts;

import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.properties.JbstProperties;
import jbst.foundation.domain.tuples.TuplePresence;
import jbst.iam.configurations.TestConfigurationValidators;
import jbst.iam.domain.db.InvitationCode;
import jbst.iam.domain.dto.requests.RequestNewInvitationCodeParams;
import jbst.iam.domain.identifiers.InvitationId;
import jbst.iam.repositories.InvitationCodesRepository;
import jbst.iam.validators.BaseInvitationCodesRequestsValidator;
import jbst.iam.validators.abtracts.AbstractBaseInvitationCodesRequestsValidator;
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

import java.util.Set;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static jbst.foundation.domain.base.AbstractAuthority.*;
import static jbst.foundation.utilities.exceptions.ExceptionsMessagesUtility.entityAccessDenied;
import static jbst.foundation.utilities.exceptions.ExceptionsMessagesUtility.entityNotFound;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AbstractBaseInvitationCodesRequestsValidatorTest {

    private static Stream<Arguments> validateCreateNewInvitationCodeTest() {
        return Stream.of(
                Arguments.of(new RequestNewInvitationCodeParams(Set.of(INVITATIONS_READ, "invitationCode:send")), "Authorities must contains: [admin, invitations:read, invitations:write, user]"),
                Arguments.of(new RequestNewInvitationCodeParams(Set.of(INVITATIONS_READ, SUPERADMIN)), "Authorities must contains: [admin, invitations:read, invitations:write, user]"),
                Arguments.of(new RequestNewInvitationCodeParams(Set.of()), null),
                Arguments.of(new RequestNewInvitationCodeParams(Set.of(INVITATIONS_READ, INVITATIONS_WRITE)), null)
        );
    }

    @Configuration
    @Import({
            TestConfigurationValidators.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final InvitationCodesRepository invitationCodesRepository;
        private final JbstProperties jbstProperties;

        @Bean
        BaseInvitationCodesRequestsValidator baseInvitationCodesRequestsValidator() {
            return new AbstractBaseInvitationCodesRequestsValidator(
                    this.invitationCodesRepository,
                    this.jbstProperties
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
        var invitationCodeId = InvitationId.random();
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
        var invitationCodeId = InvitationId.random();
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
        var invitationCodeId = InvitationId.random();
        var invitationCode = InvitationCode.random();
        when(this.invitationCodesRepository.isPresent(invitationCodeId)).thenReturn(TuplePresence.present(invitationCode));

        // Act
        this.componentUnderTest.validateDeleteById(invitationCode.owner(), invitationCodeId);

        // Assert
        verify(this.invitationCodesRepository).isPresent(invitationCodeId);
    }
}
