
package jbst.iam.resources.base;

import jbst.iam.assistants.current.CurrentSessionAssistant;
import jbst.iam.domain.dto.requests.RequestNewInvitationCodeParams;
import jbst.iam.domain.dto.responses.ResponseInvitationCode;
import jbst.iam.domain.dto.responses.ResponseInvitationCodes;
import jbst.iam.domain.identifiers.InvitationCodeId;
import jbst.iam.services.BaseInvitationCodesService;
import jbst.iam.configurations.TestRunnerResources1;
import jbst.iam.validators.BaseInvitationCodesRequestsValidator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import jbst.foundation.domain.base.Username;
import jbst.foundation.domain.properties.JbstProperties;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static jbst.foundation.utilities.random.EntityUtility.entity;
import static jbst.foundation.utilities.random.EntityUtility.list345;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BaseSecurityInvitationCodesResourceTest extends TestRunnerResources1 {

    // Assistants
    private final CurrentSessionAssistant currentSessionAssistant;
    // Services
    private final BaseInvitationCodesService baseInvitationCodesService;
    // Validators
    private final BaseInvitationCodesRequestsValidator baseInvitationCodesRequestsValidator;
    // Properties
    private final JbstProperties jbstProperties;

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
        var authorities = this.jbstProperties.getSecurityJwtConfigs().getAuthoritiesConfigs().getAvailableAuthorities();
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
        when(this.currentSessionAssistant.getCurrentUsername()).thenReturn(Username.hardcoded());
        var request = RequestNewInvitationCodeParams.hardcoded();

        // Act
        this.mvc.perform(
                post("/invitationCodes")
                        .content(this.objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        // Assert
        verify(this.currentSessionAssistant).getCurrentUsername();
        verify(this.baseInvitationCodesRequestsValidator).validateCreateNewInvitationCode(request);
        verify(this.baseInvitationCodesService).save(Username.hardcoded(), request);
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
