package io.tech1.framework.b2b.mongodb.security.jwt.validators.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserLogin;
import io.tech1.framework.b2b.mongodb.security.jwt.tests.contexts.TestsApplicationValidatorsContext;
import io.tech1.framework.b2b.mongodb.security.jwt.validators.AuthenticationRequestsValidator;
import io.tech1.framework.domain.base.Password;
import io.tech1.framework.domain.base.Username;
import lombok.RequiredArgsConstructor;
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

import java.util.stream.Stream;

import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;
import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationRequestsValidatorImplTest {

    @Configuration
    @Import({
            TestsApplicationValidatorsContext.class
    })
    static class ContextConfiguration {

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
    public void validateLoginRequestTest(RequestUserLogin requestUserLogin, String exceptionMessage) {
        // Act
        var throwable = catchThrowable(() -> this.componentUnderTest.validateLoginRequest(requestUserLogin));

        // Assert
        if (nonNull(exceptionMessage)) {
            assertThat(throwable).isNotNull();
            assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
            assertThat(throwable).hasMessage(exceptionMessage);
        } else {
            assertThat(throwable).isNull();
        }
    }
}
