package io.tech1.framework.iam.resources;

import io.tech1.framework.iam.tests.runners.AbstractResourcesRunner1;
import io.tech1.framework.iam.assistants.current.CurrentSessionAssistant;
import io.tech1.framework.iam.domain.db.UserSession;
import io.tech1.framework.iam.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.iam.domain.dto.responses.ResponseUserSessionsTable;
import io.tech1.framework.iam.domain.identifiers.UserSessionId;
import io.tech1.framework.iam.domain.jwt.RequestAccessToken;
import io.tech1.framework.iam.domain.security.CurrentClientUser;
import io.tech1.framework.iam.services.BaseUsersSessionsService;
import io.tech1.framework.iam.tokens.facade.TokensProvider;
import io.tech1.framework.iam.validators.BaseUsersSessionsRequestsValidator;
import io.tech1.framework.foundation.domain.base.Username;
import lombok.RequiredArgsConstructor;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.HttpServletRequest;
import java.time.ZoneId;

import static io.tech1.framework.foundation.utilities.random.EntityUtility.list345;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BaseSecurityUsersSessionsResourceTest extends AbstractResourcesRunner1 {

    // Assistants
    private final CurrentSessionAssistant currentSessionAssistant;
    // Services
    private final BaseUsersSessionsService baseUsersSessionsService;
    // Tokens
    private final TokensProvider tokensProvider;
    // Validators
    private final BaseUsersSessionsRequestsValidator baseUsersSessionsRequestsValidator;

    // Resource
    private final BaseSecurityUsersSessionsResource componentUnderTest;

    @BeforeEach
    void beforeEach() {
        this.standaloneSetupByResourceUnderTest(this.componentUnderTest);
        reset(
                this.currentSessionAssistant,
                this.baseUsersSessionsService,
                this.tokensProvider,
                this.baseUsersSessionsRequestsValidator
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.currentSessionAssistant,
                this.baseUsersSessionsService,
                this.tokensProvider,
                this.baseUsersSessionsRequestsValidator
        );
    }

    @Test
    void getSessionsTableTest() throws Exception {
        // Arrange
        var userSessionsTables = ResponseUserSessionsTable.of(list345(ResponseUserSession2.class));
        var requestAccessToken = RequestAccessToken.random();
        when(this.tokensProvider.readRequestAccessToken(any(HttpServletRequest.class))).thenReturn(requestAccessToken);
        when(this.currentSessionAssistant.getCurrentUserDbSessionsTable(requestAccessToken)).thenReturn(userSessionsTables);

        // Act
        this.mvc.perform(get("/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessions", hasSize(userSessionsTables.sessions().size())))
                .andExpect(jsonPath("$.anyPresent", instanceOf(Boolean.class)))
                .andExpect(jsonPath("$.anyProblem", instanceOf(Boolean.class)));

        // Assert
        verify(this.tokensProvider).readRequestAccessToken(any(HttpServletRequest.class));
        verify(this.currentSessionAssistant).getCurrentUserDbSessionsTable(requestAccessToken);
    }

    @Test
    void getCurrentClientUserCronEnabledTest() throws Exception {
        // Arrange
        var currentClientUser = CurrentClientUser.random();
        var session = UserSession.randomPersistedSession();
        when(this.currentSessionAssistant.getCurrentClientUser()).thenReturn(currentClientUser);
        when(this.currentSessionAssistant.getCurrentUserSession(any(HttpServletRequest.class))).thenReturn(session);

        // Act
        this.mvc.perform(get("/sessions/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(currentClientUser.getUsername().value()))
                .andExpect(jsonPath("$.email").value(currentClientUser.getEmail().value()))
                .andExpect(jsonPath("$.name").value(currentClientUser.getName()))
                .andExpect(jsonPath("$.zoneId").value(currentClientUser.getZoneId().getId()))
                .andExpect(jsonPath("$.authorities").isEmpty())
                .andExpect(jsonPath("$.attributes").isEmpty())
                .andExpect(jsonPath("$.zoneId", new BaseMatcher<String>() {

                    @Override
                    public void describeTo(Description description) {
                        // no actions
                    }

                    @Override
                    public boolean matches(Object o) {
                        var zoneId = ZoneId.of(o.toString());
                        return ZoneId.getAvailableZoneIds().contains(zoneId.getId());
                    }
                }));

        // Assert
        verify(this.currentSessionAssistant).getCurrentClientUser();
        verify(this.currentSessionAssistant).getCurrentUserSession(any(HttpServletRequest.class));
        verify(this.baseUsersSessionsService).renewUserRequestMetadata(eq(session), any(HttpServletRequest.class));
    }

    @Test
    void renewManuallyTest() throws Exception {
        // Arrange
        when(this.currentSessionAssistant.getCurrentUsername()).thenReturn(Username.testsHardcoded());

        // Act
        this.mvc.perform(
                        post("/sessions/" + UserSessionId.testsHardcoded() + "/renew/manually")
                )
                .andExpect(status().isOk());

        // Assert
        verify(this.currentSessionAssistant).getCurrentUsername();
        verify(this.baseUsersSessionsService).assertAccess(Username.testsHardcoded(), UserSessionId.testsHardcoded());
        verify(this.baseUsersSessionsService).enableUserRequestMetadataRenewManually(UserSessionId.testsHardcoded());
    }

    @Test
    void deleteByIdTest() throws Exception {
        // Arrange
        when(this.currentSessionAssistant.getCurrentUsername()).thenReturn(Username.testsHardcoded());

        // Act
        this.mvc.perform(delete("/sessions/" + UserSessionId.testsHardcoded()))
                .andExpect(status().isOk());

        // Assert
        verify(this.currentSessionAssistant).getCurrentUsername();
        verify(this.baseUsersSessionsService).assertAccess(Username.testsHardcoded(), UserSessionId.testsHardcoded());
        verify(this.baseUsersSessionsService).deleteById(UserSessionId.testsHardcoded());
    }

    @Test
    void deleteAllExceptCurrent() throws Exception {
        // Arrange
        when(this.currentSessionAssistant.getCurrentUsername()).thenReturn(Username.testsHardcoded());
        when(this.tokensProvider.readRequestAccessToken(any(HttpServletRequest.class))).thenReturn(RequestAccessToken.testsHardcoded());

        // Act
        this.mvc.perform(delete("/sessions"))
                .andExpect(status().isOk());

        // Assert
        verify(this.currentSessionAssistant).getCurrentUsername();
        verify(this.tokensProvider).readRequestAccessToken(any(HttpServletRequest.class));
        verify(this.baseUsersSessionsService).deleteAllExceptCurrent(Username.testsHardcoded(), RequestAccessToken.testsHardcoded());
    }
}
