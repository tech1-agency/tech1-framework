
package io.tech1.framework.b2b.base.security.jwt.resources;

import io.tech1.framework.b2b.base.security.jwt.assistants.current.CurrentSessionAssistant;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCodes;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.InvitationCodeId;
import io.tech1.framework.b2b.base.security.jwt.services.BaseInvitationCodesService;
import io.tech1.framework.b2b.base.security.jwt.tests.runners.AbstractResourcesRunner1;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseInvitationCodesRequestsValidator;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.EntityUtility.list345;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomUsername;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BaseSecurityInvitationCodesResourceTest extends AbstractResourcesRunner1 {

    // Assistants
    private final CurrentSessionAssistant currentSessionAssistant;
    // Services
    private final BaseInvitationCodesService baseInvitationCodesService;
    // Validators
    private final BaseInvitationCodesRequestsValidator baseInvitationCodesRequestsValidator;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    // Resource
    private final BaseSecurityInvitationCodesResource componentUnderTest;

    @BeforeEach
    void beforeEach() {
        this.standaloneSetupByResourceUnderTest(this.componentUnderTest);
        reset(
                this.currentSessionAssistant,
                this.baseInvitationCodesService
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.currentSessionAssistant,
                this.baseInvitationCodesService
        );
    }

    @Test
    void findAllTest() throws Exception {
        // Arrange
        var owner = Username.random();
        when(this.currentSessionAssistant.getCurrentUsername()).thenReturn(owner);
        var authorities = this.applicationFrameworkProperties.getSecurityJwtConfigs().getAuthoritiesConfigs().getAvailableAuthorities();
        var invitationCodes = list345(ResponseInvitationCode.class);
        var responseInvitationCodes = new ResponseInvitationCodes(authorities, invitationCodes);
        when(this.baseInvitationCodesService.findByOwner(owner)).thenReturn(responseInvitationCodes);

        // Act
        this.mvc.perform(get("/invitationCodes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorities", hasSize(4)))
                .andExpect(jsonPath("$.invitationCodes", hasSize(invitationCodes.size())));

        // Assert
        verify(this.currentSessionAssistant).getCurrentUsername();
        verify(this.baseInvitationCodesService).findByOwner(owner);
    }

    @Test
    void saveTest() throws Exception {
        // Arrange
        var owner = Username.random();
        when(this.currentSessionAssistant.getCurrentUsername()).thenReturn(owner);
        var requestNewInvitationCodeParams = entity(RequestNewInvitationCodeParams.class);

        // Act
        this.mvc.perform(
                post("/invitationCodes")
                        .content(this.objectMapper.writeValueAsString(requestNewInvitationCodeParams))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        // Assert
        verify(this.currentSessionAssistant).getCurrentUsername();
        verify(this.baseInvitationCodesRequestsValidator).validateCreateNewInvitationCode(requestNewInvitationCodeParams);
        verify(this.baseInvitationCodesService).save(owner, requestNewInvitationCodeParams);
    }

    @Test
    void deleteByIdTest() throws Exception {
        // Arrange
        var username= entity(Username.class);
        var invitationCodeId = entity(InvitationCodeId.class);
        when(this.currentSessionAssistant.getCurrentUsername()).thenReturn(username);

        // Act
        this.mvc.perform(delete("/invitationCodes/" + invitationCodeId))
                .andExpect(status().isOk());

        // Assert
        verify(this.currentSessionAssistant).getCurrentUsername();
        verify(this.baseInvitationCodesRequestsValidator).validateDeleteById(username, invitationCodeId);
        verify(this.baseInvitationCodesService).deleteById(invitationCodeId);
    }
}
