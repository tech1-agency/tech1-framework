package io.tech1.framework.b2b.mongodb.security.jwt.resources;

import io.tech1.framework.b2b.mongodb.security.jwt.assistants.core.CurrentSessionAssistant;
import io.tech1.framework.b2b.mongodb.security.jwt.cookies.CookieProvider;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseUserSessionsTable;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.mongodb.security.jwt.services.UserSessionService;
import io.tech1.framework.b2b.mongodb.security.jwt.tests.runnerts.AbstractResourcesRunner;
import io.tech1.framework.b2b.mongodb.security.jwt.validators.SessionsRequestsValidator;
import lombok.RequiredArgsConstructor;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.EntityUtility.list345;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BaseSecuritySessionsResourceTest extends AbstractResourcesRunner {

    // Assistants
    private final CurrentSessionAssistant currentSessionAssistant;
    // Services
    private final UserSessionService userSessionService;
    // Cookie
    private final CookieProvider cookieProvider;
    // Validators
    private final SessionsRequestsValidator sessionsRequestsValidator;

    // Resource
    private final BaseSecuritySessionsResource componentUnderTest;

    @BeforeEach
    void beforeEach() {
        this.standaloneSetupByResourceUnderTest(this.componentUnderTest);
        reset(
                this.currentSessionAssistant
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.currentSessionAssistant
        );
    }

    @Test
    void getCurrentClientUserTest() throws Exception {
        // Arrange
        var currentClientUser = randomCurrentClientUser();
        when(this.currentSessionAssistant.getCurrentClientUser()).thenReturn(currentClientUser);

        // Act
        this.mvc.perform(get("/sessions/current").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(currentClientUser.getUsername().identifier()))
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
    }

    @Test
    void getCurrentUserDbSessionsTest() throws Exception {
        // Arrange
        var userSessionsTables = ResponseUserSessionsTable.of(list345(ResponseUserSession2.class));
        when(this.currentSessionAssistant.getCurrentUserDbSessionsTable(any(HttpServletRequest.class))).thenReturn(userSessionsTables);

        // Act
        this.mvc.perform(get("/sessions").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessions", hasSize(userSessionsTables.sessions().size())))
                .andExpect(jsonPath("$.anyPresent", instanceOf(Boolean.class)))
                .andExpect(jsonPath("$.anyProblem", instanceOf(Boolean.class)));

        // Assert
        verify(this.currentSessionAssistant).getCurrentUserDbSessionsTable(any(HttpServletRequest.class));
    }

    @Test
    void deleteByIdTest() throws Exception {
        // Arrange
        var user = entity(DbUser.class);
        when(this.currentSessionAssistant.getCurrentDbUser()).thenReturn(user);
        var sessionId = randomString();

        // Act
        this.mvc.perform(
                        delete("/sessions/" + sessionId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        // Assert
        verify(this.currentSessionAssistant).getCurrentDbUser();
        verify(this.sessionsRequestsValidator).validateDeleteById(user, sessionId);
        verify(this.userSessionService).deleteById(sessionId);
    }

    @Test
    void deleteAllExceptCurrent() throws Exception {
        // Arrange
        var user = entity(DbUser.class);
        var cookie = entity(CookieRefreshToken.class);
        when(this.currentSessionAssistant.getCurrentDbUser()).thenReturn(user);
        when(this.cookieProvider.readJwtRefreshToken(any(HttpServletRequest.class))).thenReturn(cookie);

        // Act
        this.mvc.perform(
                        delete("/sessions/")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        // Assert
        verify(this.currentSessionAssistant).getCurrentDbUser();
        verify(this.userSessionService).deleteAllExceptCurrent(user, cookie);
    }
}
