package io.tech1.framework.b2b.base.security.jwt.validators.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.db.InvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.b2b.base.security.jwt.repositories.InvitationCodesRepository;
import io.tech1.framework.b2b.base.security.jwt.tests.contexts.TestsApplicationValidatorsContext;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseInvitationCodesRequestsValidator;
import io.tech1.framework.b2b.base.security.jwt.validators.abtracts.AbstractBaseInvitationCodesRequestsValidator;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.tuples.TuplePresence;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Set;
import java.util.stream.Stream;

import static io.tech1.framework.domain.base.AbstractAuthority.*;
import static java.util.Objects.nonNull;
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
                Arguments.of(new RequestNewInvitationCodeParams(Set.of(INVITATION_CODE_READ, "invitationCode:send")), "Invitation code request params contains unsupported authority"),
                Arguments.of(new RequestNewInvitationCodeParams(Set.of(INVITATION_CODE_READ, SUPER_ADMIN)), "Invitation code request params contains unsupported authority"),
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
                .hasMessage("InvitationCode: Not Found, id = " + invitationCodeId);
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
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("InvitationCode: Access Denied, id = " + invitationCodeId.value());
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
