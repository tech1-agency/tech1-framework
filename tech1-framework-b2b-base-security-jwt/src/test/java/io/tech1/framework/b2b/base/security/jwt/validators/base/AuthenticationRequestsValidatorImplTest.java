package io.tech1.framework.b2b.base.security.jwt.validators.base;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserLogin;
import io.tech1.framework.b2b.base.security.jwt.validators.AuthenticationRequestsValidator;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AuthenticationRequestsValidatorImplTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        public AuthenticationRequestsValidator authenticationRequestsValidator() {
            return new BaseAuthenticationRequestsValidator();
        }
    }

    private static Stream<Arguments> validateLoginRequestTest() {
        return Stream.of(
                Arguments.of(
                        new RequestUserLogin(null, Password.of("admin")),
                        invalidAttribute("username")
                ),
                Arguments.of(
                        new RequestUserLogin(Username.of("admin"), null),
                        invalidAttribute("password")
                ),
                Arguments.of(
                        new RequestUserLogin(Username.of("admin"), Password.of("admin")), null
                ),
                Arguments.of(
                        new RequestUserLogin(Username.of("user"), Password.of("password")), null
                )
        );
    }

    private final AuthenticationRequestsValidator componentUnderTest;

    @ParameterizedTest
    @MethodSource("validateLoginRequestTest")
    void validateLoginRequestTest(RequestUserLogin requestUserLogin, String exceptionMessage) {
        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateLoginRequest(requestUserLogin));

        // Assert
        if (nonNull(exceptionMessage)) {
            assertThat(throwable)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(exceptionMessage);
        } else {
            assertThat(throwable).isNull();
        }
    }
}
