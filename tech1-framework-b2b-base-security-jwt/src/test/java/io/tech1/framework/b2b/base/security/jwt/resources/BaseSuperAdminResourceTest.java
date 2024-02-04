package io.tech1.framework.b2b.base.security.jwt.resources;

import io.tech1.framework.b2b.base.security.jwt.assistants.current.CurrentSessionAssistant;
import io.tech1.framework.b2b.base.security.jwt.tokens.facade.TokensProvider;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseInvitationCode;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseSuperadminSessionsTable;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.RequestAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.services.BaseSuperAdminService;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersSessionsService;
import io.tech1.framework.b2b.base.security.jwt.tests.runners.AbstractResourcesRunner1;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

import static io.tech1.framework.b2b.base.security.jwt.tests.random.BaseSecurityJwtRandomUtility.randomResetServerStatus;
import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.EntityUtility.list345;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BaseSuperAdminResourceTest extends AbstractResourcesRunner1 {

    // Assistants
    private final CurrentSessionAssistant currentSessionAssistant;
    // Services
    private final BaseSuperAdminService baseSuperAdminService;
    private final BaseUsersSessionsService baseUsersSessionsService;
    // Tokens
    private final TokensProvider tokensProvider;

    // Resource
    private final BaseSuperAdminResource componentUnderTest;

    @BeforeEach
    void beforeEach() {
        this.standaloneSetupByResourceUnderTest(this.componentUnderTest);
        reset(
                this.currentSessionAssistant,
                this.baseSuperAdminService,
                this.baseUsersSessionsService,
                this.tokensProvider
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.currentSessionAssistant,
                this.baseSuperAdminService,
                this.baseUsersSessionsService,
                this.tokensProvider
        );
    }

    @Test
    void getResetServerStatusTest() throws Exception {
        // Arrange
        when(this.baseSuperAdminService.getResetServerStatus()).thenReturn(randomResetServerStatus());

        // Act
        this.mvc.perform(get("/superadmin/server/reset/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").doesNotExist())
                .andExpect(jsonPath("$.stage").doesNotExist())
                .andExpect(jsonPath("$.stagesCount").doesNotExist())
                .andExpect(jsonPath("$.percentage").exists())
                .andExpect(jsonPath("$.description").exists());

        // Assert
        verify(this.baseSuperAdminService).getResetServerStatus();
    }

    @Test
    void resetServerTest() throws Exception {
        // Arrange
        var user = entity(JwtUser.class);
        when(this.currentSessionAssistant.getCurrentJwtUser()).thenReturn(user);

        // Act
        this.mvc.perform(post("/superadmin/server/reset"))
                .andExpect(status().isOk());

        // Assert
        verify(this.currentSessionAssistant).getCurrentJwtUser();
        verify(this.baseSuperAdminService).resetServerBy(user);
    }

    @Test
    void getUnusedInvitationCodesTest() throws Exception {
        // Arrange
        var codes = list345(ResponseInvitationCode.class);
        when(this.baseSuperAdminService.findUnused()).thenReturn(codes);

        // Act
        this.mvc.perform(get("/superadmin/invitationCodes/unused"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(codes.size())))
                .andExpect(jsonPath("$.[0].id", notNullValue()))
                .andExpect(jsonPath("$.[0].owner", notNullValue()))
                .andExpect(jsonPath("$.[0].authorities", notNullValue()))
                .andExpect(jsonPath("$.[0].value", notNullValue()))
                .andExpect(jsonPath("$.[0].invited", notNullValue()));

        // Assert
        verify(this.baseSuperAdminService).findUnused();
    }

    @Test
    void getSessionsTest() throws Exception {
        // Arrange
        var sessionsTable = new ResponseSuperadminSessionsTable(
                list345(ResponseUserSession2.class),
                list345(ResponseUserSession2.class)
        );
        var requestAccessToken = RequestAccessToken.random();
        when(this.tokensProvider.readRequestAccessToken(any(HttpServletRequest.class))).thenReturn(requestAccessToken);
        when(this.baseSuperAdminService.getSessions(requestAccessToken)).thenReturn(sessionsTable);

        // Act
        this.mvc.perform(get("/superadmin/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activeSessions", hasSize(sessionsTable.activeSessions().size())))
                .andExpect(jsonPath("$.activeSessions.[0].id", notNullValue()))
                .andExpect(jsonPath("$.activeSessions.[0].who", notNullValue()))
                .andExpect(jsonPath("$.activeSessions.[0].current", notNullValue()))
                .andExpect(jsonPath("$.activeSessions.[0].activity", notNullValue()))
                .andExpect(jsonPath("$.activeSessions.[0].exception", notNullValue()))
                .andExpect(jsonPath("$.activeSessions.[0].ipAddr", notNullValue()))
                .andExpect(jsonPath("$.activeSessions.[0].countryFlag", notNullValue()))
                .andExpect(jsonPath("$.activeSessions.[0].where", notNullValue()))
                .andExpect(jsonPath("$.activeSessions.[0].browser", notNullValue()))
                .andExpect(jsonPath("$.activeSessions.[0].what", notNullValue()))
                .andExpect(jsonPath("$.inactiveSessions", hasSize(sessionsTable.inactiveSessions().size())))
                .andExpect(jsonPath("$.inactiveSessions.[0].id", notNullValue()))
                .andExpect(jsonPath("$.inactiveSessions.[0].who", notNullValue()))
                .andExpect(jsonPath("$.inactiveSessions.[0].current", notNullValue()))
                .andExpect(jsonPath("$.inactiveSessions.[0].activity", notNullValue()))
                .andExpect(jsonPath("$.inactiveSessions.[0].exception", notNullValue()))
                .andExpect(jsonPath("$.inactiveSessions.[0].ipAddr", notNullValue()))
                .andExpect(jsonPath("$.inactiveSessions.[0].countryFlag", notNullValue()))
                .andExpect(jsonPath("$.inactiveSessions.[0].where", notNullValue()))
                .andExpect(jsonPath("$.inactiveSessions.[0].browser", notNullValue()))
                .andExpect(jsonPath("$.inactiveSessions.[0].what", notNullValue()));

        // Assert
        verify(this.tokensProvider).readRequestAccessToken(any(HttpServletRequest.class));
        verify(this.baseSuperAdminService).getSessions(requestAccessToken);
    }

    @Test
    void renewManuallyTest() throws Exception {
        // Arrange
        var sessionId = UserSessionId.random();

        // Act
        this.mvc.perform(post("/superadmin/sessions/" + sessionId + "/renew/manually"))
                .andExpect(status().isOk());

        // Assert
        verify(this.baseUsersSessionsService).enableUserRequestMetadataRenewManually(sessionId);
    }

    @Test
    void deleteByIdTest() throws Exception {
        // Arrange
        var sessionId = UserSessionId.random();

        // Act
        this.mvc.perform(delete("/superadmin/sessions/" + sessionId))
                .andExpect(status().isOk());

        // Assert
        verify(this.baseUsersSessionsService).deleteById(sessionId);
    }

    @Test
    void deleteAllExceptCurrentTest() throws Exception {
        // Arrange
        var requestAccessToken = RequestAccessToken.random();
        when(this.tokensProvider.readRequestAccessToken(any(HttpServletRequest.class))).thenReturn(requestAccessToken);

        // Act
        this.mvc.perform(delete("/superadmin/sessions/"))
                .andExpect(status().isOk());

        // Assert
        verify(this.tokensProvider).readRequestAccessToken(any(HttpServletRequest.class));
        verify(this.baseUsersSessionsService).deleteAllExceptCurrentAsSuperuser(requestAccessToken);
    }
}
