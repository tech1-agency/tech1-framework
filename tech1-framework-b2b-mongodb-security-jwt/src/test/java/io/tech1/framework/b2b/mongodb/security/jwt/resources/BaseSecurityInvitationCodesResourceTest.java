
package io.tech1.framework.b2b.mongodb.security.jwt.resources;

import io.tech1.framework.b2b.base.security.jwt.domain.dto.requests.RequestNewInvitationCodeParams;
import io.tech1.framework.b2b.base.security.jwt.assistants.current.CurrentSessionAssistant;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbInvitationCode;
import io.tech1.framework.b2b.mongodb.security.jwt.services.InvitationCodeService;
import io.tech1.framework.b2b.mongodb.security.jwt.tests.runnerts.AbstractResourcesRunner;
import io.tech1.framework.b2b.base.security.jwt.validators.InvitationCodeRequestsValidator;
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
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomUsername;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BaseSecurityInvitationCodesResourceTest extends AbstractResourcesRunner {

    // Assistants
    private final CurrentSessionAssistant currentSessionAssistant;
    // Services
    private final InvitationCodeService invitationCodeService;
    // Validators
    private final InvitationCodeRequestsValidator invitationCodeRequestsValidator;
    // Properties
    private final ApplicationFrameworkProperties applicationFrameworkProperties;

    // Resource
    private final BaseSecurityInvitationCodesResource componentUnderTest;

    @BeforeEach
    void beforeEach() {
        this.standaloneSetupByResourceUnderTest(this.componentUnderTest);
        reset(
                this.currentSessionAssistant,
                this.invitationCodeService
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.currentSessionAssistant,
                this.invitationCodeService
        );
    }

    @Test
    void findAllTest() throws Exception {
        // Arrange
        var owner = randomUsername();
        when(this.currentSessionAssistant.getCurrentUsername()).thenReturn(owner);
        var authorities = this.applicationFrameworkProperties.getSecurityJwtConfigs().getAuthoritiesConfigs().getAvailableAuthorities();
        var invitationCodes = list345(DbInvitationCode.class);
        // TODO [YY] fixme
//        var responseInvitationCodes = new ResponseInvitationCodes(authorities, invitationCodes);
//        when(this.invitationCodeService.findByOwner(owner)).thenReturn(responseInvitationCodes);

        // Act
        this.mvc.perform(get("/invitationCodes").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorities", hasSize(4)))
                .andExpect(jsonPath("$.invitationCodes", hasSize(invitationCodes.size())));

        // Assert
        verify(this.currentSessionAssistant).getCurrentUsername();
        verify(this.invitationCodeService).findByOwner(owner);
    }

    @Test
    void saveTest() throws Exception {
        // Arrange
        var owner = randomUsername();
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
        verify(this.invitationCodeRequestsValidator).validateCreateNewInvitationCode(requestNewInvitationCodeParams);
        verify(this.invitationCodeService).save(requestNewInvitationCodeParams, owner);
    }

    @Test
    void deleteByIdTest() throws Exception {
        // Arrange
        var username= entity(Username.class);
        when(this.currentSessionAssistant.getCurrentUsername()).thenReturn(username);
        var invitationCodeId = randomString();

        // Act
        this.mvc.perform(
                        delete("/invitationCodes/" + invitationCodeId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        // Assert
        verify(this.currentSessionAssistant).getCurrentUsername();
        verify(this.invitationCodeRequestsValidator).validateDeleteById(username, invitationCodeId);
        verify(this.invitationCodeService).deleteById(invitationCodeId);
    }
}
