package jbst.iam.resources.base;

import jbst.foundation.utilities.random.RandomUtility;
import jbst.iam.configurations.TestRunnerResources1;
import jbst.iam.services.BaseUsersService;
import jbst.iam.services.BaseUsersTokensService;
import jbst.iam.validators.BaseUsersTokensRequestsValidator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BaseSecurityUsersTokensResourceTest extends TestRunnerResources1 {

    // Services
    private final BaseUsersTokensService baseUsersTokensService;
    private final BaseUsersService baseUsersService;
    // Validators
    private final BaseUsersTokensRequestsValidator baseUsersTokensRequestsValidator;

    // Resource
    private final BaseSecurityUsersTokensResource componentUnderTest;

    @BeforeEach
    void beforeEach() {
        this.standaloneSetupByResourceUnderTest(this.componentUnderTest);
        reset(
                this.baseUsersTokensService,
                this.baseUsersService
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.baseUsersTokensService,
                this.baseUsersService
        );
    }

    @Test
    void confirmEmailTest() throws Exception {
        // Arrange
        var token = RandomUtility.randomStringLetterOrNumbersOnly(36);

        // Act
        this.mvc.perform(
                get("/tokens/email/confirm?token=%s".formatted(token))
        ).andExpect(status().isOk());

        // Assert
        verify(this.baseUsersTokensRequestsValidator).validateEmailConfirmationToken(token);
        verify(this.baseUsersTokensService).confirmEmail(token);
    }

}