package jbst.iam.resources.base;

import jbst.iam.domain.dto.requests.RequestUserRegistration1;
import jbst.iam.domain.events.EventRegistration1;
import jbst.iam.events.publishers.SecurityJwtIncidentPublisher;
import jbst.iam.events.publishers.SecurityJwtPublisher;
import jbst.iam.services.BaseRegistrationService;
import jbst.iam.configurations.TestRunnerResources1;
import jbst.iam.validators.BaseRegistrationRequestsValidator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import jbst.foundation.incidents.domain.registration.IncidentRegistration1;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BaseSecurityRegistrationResourceTest extends TestRunnerResources1 {

    // Services
    private final BaseRegistrationService baseRegistrationService;
    // Publishers
    private final SecurityJwtPublisher securityJwtPublisher;
    private final SecurityJwtIncidentPublisher securityJwtIncidentPublisher;
    // Validators
    private final BaseRegistrationRequestsValidator baseRegistrationRequestsValidator;

    // Resource
    private final BaseSecurityRegistrationResource componentUnderTest;

    @BeforeEach
    void beforeEach() {
        this.standaloneSetupByResourceUnderTest(this.componentUnderTest);
        reset(
                this.baseRegistrationService,
                this.securityJwtPublisher,
                this.securityJwtIncidentPublisher,
                this.baseRegistrationRequestsValidator
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.baseRegistrationService,
                this.securityJwtPublisher,
                this.securityJwtIncidentPublisher,
                this.baseRegistrationRequestsValidator
        );
    }

    @Test
    void register1() throws Exception {
        // Arrange
        var requestUserRegistration1 = RequestUserRegistration1.hardcoded();

        // Act
        this.mvc.perform(
                post("/registration/register1")
                        .content(this.objectMapper.writeValueAsString(requestUserRegistration1))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        // Assert
        verify(this.baseRegistrationRequestsValidator).validateRegistrationRequest1(requestUserRegistration1);
        verify(this.baseRegistrationService).register1(requestUserRegistration1);
        verify(this.securityJwtPublisher).publishRegistration1(new EventRegistration1(requestUserRegistration1));
        verify(this.securityJwtIncidentPublisher).publishRegistration1(new IncidentRegistration1(requestUserRegistration1.username()));
    }
}
