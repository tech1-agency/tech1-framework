package jbst.iam.resources.base;

import jbst.foundation.domain.exceptions.tokens.UserEmailConfirmException;
import jbst.foundation.domain.exceptions.tokens.UserTokenValidationException;
import jbst.foundation.incidents.events.publishers.IncidentPublisher;
import jbst.foundation.utilities.random.RandomUtility;
import jbst.iam.configurations.TestRunnerResources1;
import jbst.iam.services.BaseUsersTokensService;
import jbst.iam.validators.BaseUsersTokensRequestsValidator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.stubbing.Stubber;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BaseSecurityUsersTokensResourceTest extends TestRunnerResources1 {

    private static Stream<Arguments> confirmEmailTest() {
        return Stream.of(
                Arguments.of(
                        UserTokenValidationException.expired(), null, null, 0
                ),
                Arguments.of(
                        null, UserEmailConfirmException.tokenNotFound(), null, 0
                ),
                Arguments.of(
                        null, null, new IllegalArgumentException(), 0
                ),
                Arguments.of(
                        null, null, null, 1
                )
        );
    }

    // Services
    private final BaseUsersTokensService baseUsersTokensService;
    // Validators
    private final BaseUsersTokensRequestsValidator baseUsersTokensRequestsValidator;
    // Incidents
    private final IncidentPublisher incidentPublisher;

    // Resource
    private final BaseSecurityUsersTokensResource componentUnderTest;

    @BeforeEach
    void beforeEach() {
        this.standaloneSetupByResourceUnderTest(this.componentUnderTest);
        reset(
                this.baseUsersTokensService,
                this.baseUsersTokensRequestsValidator,
                this.incidentPublisher
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.baseUsersTokensService,
                this.baseUsersTokensRequestsValidator,
                this.incidentPublisher
        );
    }

    @ParameterizedTest
    @MethodSource("confirmEmailTest")
    void confirmEmailTest(
            UserTokenValidationException validationException,
            UserEmailConfirmException confirmException,
            RuntimeException runtimeException,
            int code
    ) throws Exception {
        // Arrange
        var token = RandomUtility.randomStringLetterOrNumbersOnly(36);
        Function<Exception, Stubber> doThrowNonNull = ex -> nonNull(ex) ? doThrow(ex) : doNothing();
        doThrowNonNull.apply(validationException).when(this.baseUsersTokensRequestsValidator).validateEmailConfirmationToken(token);
        doThrowNonNull.apply(confirmException).when(this.baseUsersTokensService).confirmEmail(token);
        if (nonNull(runtimeException)) {
            doThrow(runtimeException).when(this.baseUsersTokensService).confirmEmail(token);
        }
        var expectedLocation = "http://127.0.0.1:3000/email-confirm?code=" + code;

        // Act
        this.mvc.perform(
                        get("/tokens/email/confirm?token=%s".formatted(token))
                ).andExpect(status().isFound())
                .andExpect(header().string("Location", expectedLocation));

        // Assert
        verify(this.baseUsersTokensRequestsValidator).validateEmailConfirmationToken(token);
        var tokensServiceInvoked = Stream.of(nonNull(confirmException), nonNull(runtimeException), code == 1).anyMatch(b -> b);
        var confirmEmailInvocations = tokensServiceInvoked ? times(1) : times(0);
        verify(this.baseUsersTokensService, confirmEmailInvocations).confirmEmail(token);
        if (nonNull(runtimeException)) {
            verify(this.incidentPublisher).publishThrowable(any());
        }
    }

}