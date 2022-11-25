package io.tech1.framework.b2b.mongodb.security.jwt.resources;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseInvitationCode1;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseServerSessionsTable;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseUserSession3;
import io.tech1.framework.b2b.mongodb.security.jwt.services.SuperAdminService;
import io.tech1.framework.b2b.mongodb.security.jwt.tests.runnerts.AbstractResourcesRunner;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static io.tech1.framework.domain.utilities.random.EntityUtility.list345;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SuperAdminResourceTest extends AbstractResourcesRunner {

    // Assistants
    private final SuperAdminService superAdminService;

    // Resource
    private final SuperAdminResource componentUnderTest;

    @BeforeEach
    public void beforeEach() throws Exception {
        this.standaloneSetupByResourceUnderTest(this.componentUnderTest);
        reset(
                this.superAdminService
        );
    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(
                this.superAdminService
        );
    }

    @Test
    public void getUnusedInvitationCodesTest() throws Exception {
        // Arrange
        var codes = list345(ResponseInvitationCode1.class);
        when(this.superAdminService.findUnused()).thenReturn(codes);

        // Act
        this.mvc.perform(get("/superadmin/invitationCodes/unused").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(codes.size())));

        // Assert
        verify(this.superAdminService).findUnused();
    }

    @Test
    public void getServerSessions() throws Exception {
        // Arrange
        var sessionsTable = new ResponseServerSessionsTable(
                list345(ResponseUserSession3.class),
                list345(ResponseUserSession3.class)
        );
        when(this.superAdminService.getServerSessions()).thenReturn(sessionsTable);

        // Act
        this.mvc.perform(get("/superadmin/sessions/server").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activeSessions", hasSize(sessionsTable.getActiveSessions().size())))
                .andExpect(jsonPath("$.inactiveSessions", hasSize(sessionsTable.getInactiveSessions().size())));

        // Assert
        verify(this.superAdminService).getServerSessions();
    }
}
