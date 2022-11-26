package io.tech1.framework.b2b.mongodb.security.jwt.resources;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.requests.RequestUserRegistration1;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.EventRegistrationRegister1;
import io.tech1.framework.b2b.mongodb.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.mongodb.security.jwt.services.RegistrationService;
import io.tech1.framework.b2b.mongodb.security.jwt.tests.runnerts.AbstractResourcesRunner;
import io.tech1.framework.b2b.mongodb.security.jwt.validators.RegistrationRequestsValidator;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.incidents.domain.registration.Register1Incident;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseSecurityRegistrationResourceTest extends AbstractResourcesRunner {

    // Services
    private final RegistrationService registrationService;
    // Publishers
    private final SecurityJwtPublisher securityJwtPublisher;
    private final IncidentPublisher incidentPublisher;
    // Validators
    private final RegistrationRequestsValidator registrationRequestsValidator;

    // Resource
    private final BaseSecurityRegistrationResource componentUnderTest;

    @BeforeEach
    public void beforeEach() throws Exception {
        this.standaloneSetupByResourceUnderTest(this.componentUnderTest);
        reset(
                this.registrationService,
                this.securityJwtPublisher,
                this.incidentPublisher,
                this.registrationRequestsValidator
        );
    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(
                this.registrationService,
                this.securityJwtPublisher,
                this.incidentPublisher,
                this.registrationRequestsValidator
        );
    }

    @Test
    public void update1Test() throws Exception {
        // Arrange
        var requestUserRegistration1 = entity(RequestUserRegistration1.class);

        // Act
        this.mvc.perform(
                post("/registration/register1")
                        .content(this.objectMapper.writeValueAsString(requestUserRegistration1))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        // Assert
        verify(this.registrationRequestsValidator).validateRegistrationRequest1(eq(requestUserRegistration1));
        verify(this.registrationService).register1(eq(requestUserRegistration1));
        verify(this.securityJwtPublisher).publishRegistrationRegister1(eq(EventRegistrationRegister1.of(requestUserRegistration1)));
        verify(this.incidentPublisher).publishRegistration1(eq(Register1Incident.of(Username.of(requestUserRegistration1.getUsername()))));
    }
}
