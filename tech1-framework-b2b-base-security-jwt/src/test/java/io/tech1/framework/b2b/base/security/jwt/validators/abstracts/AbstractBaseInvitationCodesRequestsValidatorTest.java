package io.tech1.framework.b2b.base.security.jwt.validators.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbInvitationCodesRepository;
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

import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Stream;

import static io.tech1.framework.domain.base.AbstractAuthority.*;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static java.util.Arrays.asList;
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
                Arguments.of(new RequestNewInvitationCodeParams(Collections.emptySet()), invalidAttribute("authorities")),
                Arguments.of(new RequestNewInvitationCodeParams(new HashSet<>(asList(INVITATION_CODE_READ, "invitationCode:send"))), "Invitation code request params contains unsupported authority"),
                Arguments.of(new RequestNewInvitationCodeParams(new HashSet<>(asList(INVITATION_CODE_READ, SUPER_ADMIN))), "Invitation code request params contains unsupported authority"),
                Arguments.of(new RequestNewInvitationCodeParams(new HashSet<>(asList(INVITATION_CODE_READ, INVITATION_CODE_WRITE))), null)
        );
    }

    @Configuration
    @Import({
            TestsApplicationValidatorsContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final AnyDbInvitationCodesRepository invitationCodesRepository;
        private final ApplicationFrameworkProperties applicationFrameworkProperties;

        @Bean
        BaseInvitationCodesRequestsValidator baseInvitationCodesRequestsValidator() {
            return new AbstractBaseInvitationCodesRequestsValidator(
                    this.invitationCodesRepository,
                    this.applicationFrameworkProperties
            ) {};
        }
    }

    private final AnyDbInvitationCodesRepository invitationCodesRepository;

    private final BaseInvitationCodesRequestsValidator componentUnderTest;

    @ParameterizedTest
    @MethodSource("validateCreateNewInvitationCodeTest")
    void validateCreateNewInvitationCodeTest(RequestNewInvitationCodeParams requestNewInvitationCodeParams, String exceptionMessage) {
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
    void validateDeleteByIdNotFoundTest() {
        // Arrange
        var username = entity(Username.class);
        var invitationCodeId = entity(InvitationCodeId.class);
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
        var username = entity(Username.class);
        var invitationCodeId = entity(InvitationCodeId.class);
        var dbInvitationCode = entity(AnyDbInvitationCode.class);
        when(this.invitationCodesRepository.isPresent(invitationCodeId)).thenReturn(TuplePresence.present(dbInvitationCode));

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
        var invitationCodeId = entity(InvitationCodeId.class);
        var dbInvitationCode = entity(AnyDbInvitationCode.class);
        when(this.invitationCodesRepository.isPresent(invitationCodeId)).thenReturn(TuplePresence.present(dbInvitationCode));

        // Act
        this.componentUnderTest.validateDeleteById(dbInvitationCode.owner(), invitationCodeId);

        // Assert
        verify(this.invitationCodesRepository).isPresent(invitationCodeId);
    }
}
