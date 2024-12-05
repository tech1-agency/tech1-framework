package jbst.iam.services.abstracts;

import jbst.foundation.domain.exceptions.tokens.UserEmailConfirmException;
import jbst.foundation.utilities.random.RandomUtility;
import jbst.iam.domain.db.UserEmailDetails;
import jbst.iam.domain.db.UserToken;
import jbst.iam.domain.jwt.JwtUser;
import jbst.iam.repositories.UsersRepository;
import jbst.iam.repositories.UsersTokensRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AbstractBaseUsersTokensServiceTest {

    private static Stream<Arguments> confirmEmailTest() {
        return Stream.of(
                Arguments.of(
                        null,
                        null,
                        times(1),
                        times(0),
                        UserEmailConfirmException.tokenNotFound()
                ),
                Arguments.of(
                        UserToken.random(),
                        null,
                        times(1),
                        times(1),
                        UserEmailConfirmException.userNotFound()
                ),
                Arguments.of(
                        UserToken.random(),
                        JwtUser.random(),
                        times(1),
                        times(1),
                        null
                )
        );
    }

    @Configuration
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {

        @Bean
        UsersTokensRepository usersTokensRepository() {
            return mock(UsersTokensRepository.class);
        }

        @Bean
        UsersRepository usersRepository() {
            return mock(UsersRepository.class);
        }

        @Bean
        AbstractBaseUsersTokensService abstractBaseUsersTokensService() {
            return new AbstractBaseUsersTokensService(
                    this.usersTokensRepository(),
                    this.usersRepository()
            ) {};
        }
    }

    private final UsersTokensRepository usersTokensRepository;
    private final UsersRepository usersRepository;

    private final AbstractBaseUsersTokensService componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.usersTokensRepository,
                this.usersRepository
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.usersTokensRepository,
                this.usersRepository
        );
    }

    @SuppressWarnings("DataFlowIssue")
    @ParameterizedTest
    @MethodSource("confirmEmailTest")
    void confirmEmailTest(
            UserToken userToken,
            JwtUser jwtUser,
            VerificationMode tokensRepositoryInvocations,
            VerificationMode usersRepositoryInvocations,
            UserEmailConfirmException exception
    ) {
        // Arrange
        var token = RandomUtility.randomStringLetterOrNumbersOnly(36);
        when(this.usersTokensRepository.findByValueAsAny(token)).thenReturn(userToken);
        if (nonNull(userToken)) {
            when(this.usersRepository.findByUsernameAsJwtUserOrNull(userToken.username())).thenReturn(jwtUser);
        }

        // Act
        var actual = catchThrowable(() -> this.componentUnderTest.confirmEmail(token));

        // Assert
        verify(this.usersTokensRepository, tokensRepositoryInvocations).findByValueAsAny(token);
        verify(this.usersRepository, usersRepositoryInvocations).findByUsernameAsJwtUserOrNull(any());
        if (nonNull(exception)) {
            assertThat(actual)
                    .isInstanceOf(UserEmailConfirmException.class)
                    .hasMessage(exception.getMessage());
        } else {
            verify(this.usersRepository).saveAs(jwtUser.withEmailDetails(UserEmailDetails.confirmed()));
            verify(this.usersTokensRepository).saveAs(userToken.withUsed(true));
        }
    }

}