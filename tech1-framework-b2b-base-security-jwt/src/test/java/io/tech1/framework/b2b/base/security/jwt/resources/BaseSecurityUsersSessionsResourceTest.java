package io.tech1.framework.b2b.base.security.jwt.resources;

import io.tech1.framework.b2b.base.security.jwt.assistants.current.CurrentSessionAssistant;
import io.tech1.framework.b2b.base.security.jwt.cookies.CookieProvider;
import io.tech1.framework.b2b.base.security.jwt.domain.db.UserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSessionsTable;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersSessionsService;
import io.tech1.framework.b2b.base.security.jwt.tests.runners.AbstractResourcesRunner1;
import io.tech1.framework.b2b.base.security.jwt.validators.BaseUsersSessionsRequestsValidator;
import io.tech1.framework.domain.base.Username;
import lombok.RequiredArgsConstructor;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.EntityUtility.list345;
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
    // Cookie
    private final CookieProvider cookieProvider;
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
                this.cookieProvider,
                this.baseUsersSessionsRequestsValidator
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.currentSessionAssistant,
                this.baseUsersSessionsService,
                this.cookieProvider,
                this.baseUsersSessionsRequestsValidator
        );
    }

    @Test
    void getSessionsTableTest() throws Exception {
        // Arrange
        var userSessionsTables = ResponseUserSessionsTable.of(list345(ResponseUserSession2.class));
        var cookie = entity(CookieAccessToken.class);
        when(this.cookieProvider.readJwtAccessToken(any(HttpServletRequest.class))).thenReturn(cookie);
        when(this.currentSessionAssistant.getCurrentUserDbSessionsTable(cookie)).thenReturn(userSessionsTables);

        // Act
        this.mvc.perform(get("/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessions", hasSize(userSessionsTables.sessions().size())))
                .andExpect(jsonPath("$.anyPresent", instanceOf(Boolean.class)))
                .andExpect(jsonPath("$.anyProblem", instanceOf(Boolean.class)));

        // Assert
        verify(this.cookieProvider).readJwtAccessToken(any(HttpServletRequest.class));
        verify(this.currentSessionAssistant).getCurrentUserDbSessionsTable(cookie);
    }

    @Test
    void getCurrentClientUserCronEnabledTest() throws Exception {
        // Arrange
        var currentClientUser = randomCurrentClientUser();
        var session = entity(UserSession.class);
        when(this.currentSessionAssistant.getCurrentClientUser()).thenReturn(currentClientUser);
        when(this.currentSessionAssistant.getCurrentUserSession(any(HttpServletRequest.class))).thenReturn(session);

        // Act
        this.mvc.perform(get("/sessions/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(currentClientUser.getUsername().identifier()))
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
        verify(this.baseUsersSessionsService).renewUserRequestMetadataCron(eq(session), any(HttpServletRequest.class));
    }

    @Test
    void renewManuallyTest() throws Exception {
        // Arrange
        var user = entity(JwtUser.class);
        var sessionId = entity(UserSessionId.class);
        when(this.currentSessionAssistant.getCurrentJwtUser()).thenReturn(user);

        // Act
        this.mvc.perform(
                        post("/sessions/" + sessionId + "/renew/manually")
                )
                .andExpect(status().isOk());

        // Assert
        verify(this.currentSessionAssistant).getCurrentJwtUser();
        verify(this.baseUsersSessionsRequestsValidator).validateAccess(user.username(), sessionId);
        verify(this.baseUsersSessionsService).enableUserRequestMetadataRenewManually(sessionId);
    }

    @Test
    void deleteByIdTest() throws Exception {
        // Arrange
        var username = entity(Username.class);
        var sessionId = entity(UserSessionId.class);
        when(this.currentSessionAssistant.getCurrentUsername()).thenReturn(username);

        // Act
        this.mvc.perform(delete("/sessions/" + sessionId))
                .andExpect(status().isOk());

        // Assert
        verify(this.currentSessionAssistant).getCurrentUsername();
        verify(this.baseUsersSessionsRequestsValidator).validateAccess(username, sessionId);
        verify(this.baseUsersSessionsService).deleteById(sessionId);
    }

    @Test
    void deleteAllExceptCurrent() throws Exception {
        // Arrange
        var username = entity(Username.class);
        var cookie = entity(CookieAccessToken.class);
        when(this.currentSessionAssistant.getCurrentUsername()).thenReturn(username);
        when(this.cookieProvider.readJwtAccessToken(any(HttpServletRequest.class))).thenReturn(cookie);

        // Act
        this.mvc.perform(delete("/sessions"))
                .andExpect(status().isOk());

        // Assert
        verify(this.currentSessionAssistant).getCurrentUsername();
        verify(this.cookieProvider).readJwtAccessToken(any(HttpServletRequest.class));
        verify(this.baseUsersSessionsService).deleteAllExceptCurrent(username, cookie);
    }
}
