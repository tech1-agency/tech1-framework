package io.tech1.framework.b2b.mongodb.security.jwt.resources;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestUserRegistration1;
import io.tech1.framework.b2b.base.security.jwt.domain.events.EventRegistration1;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtIncidentPublisher;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.base.security.jwt.resources.BaseSecurityRegistrationResource;
import io.tech1.framework.b2b.base.security.jwt.services.BaseRegistrationService;
import io.tech1.framework.b2b.mongodb.security.jwt.tests.runnerts.AbstractResourcesRunner;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseRegistrationRequestsValidator;
import io.tech1.framework.incidents.domain.registration.IncidentRegistration1;
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
class BaseSecurityRegistrationResourceTest extends AbstractResourcesRunner {

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
    void update1Test() throws Exception {
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
        verify(this.baseRegistrationRequestsValidator).validateRegistrationRequest1(requestUserRegistration1);
        verify(this.baseRegistrationService).register1(requestUserRegistration1);
        verify(this.securityJwtPublisher).publishRegistration1(new EventRegistration1(requestUserRegistration1));
        verify(this.securityJwtIncidentPublisher).publishRegistration1(new IncidentRegistration1(requestUserRegistration1.username()));
    }
}
