package io.tech1.framework.b2b.mongodb.security.jwt.sessions.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUserSession;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.EventAuthenticationLogin;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.EventAuthenticationLogout;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.EventSessionExpired;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.EventSessionRefreshed;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.session.Session;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.session.SessionsValidatedTuple2;
import io.tech1.framework.b2b.mongodb.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.mongodb.security.jwt.services.UserSessionService;
import io.tech1.framework.b2b.mongodb.security.jwt.sessions.SessionRegistry;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.tuples.Tuple2;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLogoutFull;
import io.tech1.framework.incidents.domain.authetication.IncidentAuthenticationLogoutMin;
import io.tech1.framework.incidents.domain.session.IncidentSessionExpired;
import io.tech1.framework.incidents.events.publishers.IncidentPublisher;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomUsername;
import static io.tech1.framework.domain.utilities.reflections.ReflectionUtility.setPrivateField;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SessionRegistryImplTest {

    @Configuration
    static class ContextConfiguration {
        @Bean
        IncidentPublisher incidentPublisher() {
            return mock(IncidentPublisher.class);
        }

        @Bean
        SecurityJwtPublisher securityJwtPublisher() {
            return mock(SecurityJwtPublisher.class);
        }

        @Bean
        UserSessionService userSessionService() {
            return mock(UserSessionService.class);
        }

        @Bean
        SessionRegistry sessionRegistry() {
            return new SessionRegistryImpl(
                    this.incidentPublisher(),
                    this.securityJwtPublisher(),
                    this.userSessionService()
            );
        }
    }

    private final IncidentPublisher incidentPublisher;
    private final SecurityJwtPublisher securityJwtPublisher;
    private final UserSessionService userSessionService;

    private final SessionRegistry componentUnderTest;

    @BeforeEach
    public void beforeEach() throws Exception {
        // WARNING: clean session to execute a.k.a. integration test -> method "integrationFlow"
        setPrivateField(this.componentUnderTest, "sessions", ConcurrentHashMap.newKeySet());
        reset(
                this.incidentPublisher,
                this.securityJwtPublisher,
                this.userSessionService
        );
    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(
                this.incidentPublisher,
                this.securityJwtPublisher,
                this.userSessionService
        );
    }

    @Test
    public void integrationTest() {
        // Arrange
        var session1 = Session.of(Username.of("username1"), new JwtRefreshToken(randomString()));
        var session2 = Session.of(Username.of("username2"), new JwtRefreshToken(randomString()));
        var session3 = Session.of(Username.of("username3"), new JwtRefreshToken(randomString()));
        var session4 = Session.of(Username.of("username4"), new JwtRefreshToken(randomString()));
        var rndSession = Session.of(randomUsername(), new JwtRefreshToken(randomString()));
        var dbUserSession1 = entity(DbUserSession.class);
        var dbUserSession2 = entity(DbUserSession.class);
        var dbUserSession3 = entity(DbUserSession.class);
        var dbUserSession4 = entity(DbUserSession.class);
        var rndDbUserSession = entity(DbUserSession.class);
        when(this.userSessionService.findByRefreshToken(session1.getRefreshToken())).thenReturn(dbUserSession1);
        when(this.userSessionService.findByRefreshToken(session2.getRefreshToken())).thenReturn(dbUserSession2);
        when(this.userSessionService.findByRefreshToken(session3.getRefreshToken())).thenReturn(dbUserSession3);
        when(this.userSessionService.findByRefreshToken(session4.getRefreshToken())).thenReturn(dbUserSession4);
        when(this.userSessionService.findByRefreshToken(rndSession.getRefreshToken())).thenReturn(rndDbUserSession);

        // Iteration #1
        var activeSessionsUsernames1 = this.componentUnderTest.getActiveSessionsUsernamesIdentifiers();
        assertThat(activeSessionsUsernames1).hasSize(0);
        assertThat(this.componentUnderTest.getActiveSessionsRefreshTokens()).hasSize(0);

        // Iteration #2
        this.componentUnderTest.register(session1);
        this.componentUnderTest.register(session2);
        var activeSessionsUsernames2 = this.componentUnderTest.getActiveSessionsUsernames();
        assertThat(activeSessionsUsernames2).hasSize(2);
        assertThat(this.componentUnderTest.getActiveSessionsRefreshTokens()).hasSize(2);
        assertThat(activeSessionsUsernames2).isEqualTo(new HashSet<>(List.of(session1.getUsername(), session2.getUsername())));

        // Iteration #3
        this.componentUnderTest.register(session3);
        this.componentUnderTest.logout(rndSession);
        var activeSessionsUsernames3 = this.componentUnderTest.getActiveSessionsUsernames();
        assertThat(activeSessionsUsernames3).hasSize(3);
        assertThat(this.componentUnderTest.getActiveSessionsRefreshTokens()).hasSize(3);
        assertThat(activeSessionsUsernames3).isEqualTo(new HashSet<>(List.of(session1.getUsername(), session2.getUsername(), session3.getUsername())));

        // Iteration #4
        this.componentUnderTest.register(session4);
        this.componentUnderTest.logout(session1);
        this.componentUnderTest.logout(session2);
        this.componentUnderTest.logout(session3);
        var activeSessionsUsernames4 = this.componentUnderTest.getActiveSessionsUsernames();
        assertThat(activeSessionsUsernames4).hasSize(1);
        assertThat(this.componentUnderTest.getActiveSessionsRefreshTokens()).hasSize(1);
        assertThat(activeSessionsUsernames4).isEqualTo(new HashSet<>(List.of(session4.getUsername())));

        // Iteration #5 (cleanup)
        this.componentUnderTest.logout(session4);
        assertThat(this.componentUnderTest.getActiveSessionsUsernamesIdentifiers()).hasSize(0);
        assertThat(this.componentUnderTest.getActiveSessionsUsernames()).hasSize(0);
        assertThat(this.componentUnderTest.getActiveSessionsRefreshTokens()).hasSize(0);
        verify(this.userSessionService).findByRefreshToken(eq(session1.getRefreshToken()));
        verify(this.userSessionService).findByRefreshToken(eq(session2.getRefreshToken()));
        verify(this.userSessionService).findByRefreshToken(eq(session3.getRefreshToken()));
        verify(this.userSessionService).findByRefreshToken(eq(session4.getRefreshToken()));
        verify(this.userSessionService).findByRefreshToken(eq(rndSession.getRefreshToken()));
        verify(this.securityJwtPublisher).publishAuthenticationLogin(eq(EventAuthenticationLogin.of(session1.getUsername())));
        verify(this.securityJwtPublisher).publishAuthenticationLogin(eq(EventAuthenticationLogin.of(session2.getUsername())));
        verify(this.securityJwtPublisher).publishAuthenticationLogin(eq(EventAuthenticationLogin.of(session3.getUsername())));
        verify(this.securityJwtPublisher).publishAuthenticationLogin(eq(EventAuthenticationLogin.of(session4.getUsername())));
        verify(this.securityJwtPublisher).publishAuthenticationLogout(eq(EventAuthenticationLogout.of(session1)));
        verify(this.securityJwtPublisher).publishAuthenticationLogout(eq(EventAuthenticationLogout.of(session2)));
        verify(this.securityJwtPublisher).publishAuthenticationLogout(eq(EventAuthenticationLogout.of(session3)));
        verify(this.securityJwtPublisher).publishAuthenticationLogout(eq(EventAuthenticationLogout.of(session4)));
        verify(this.securityJwtPublisher).publishAuthenticationLogout(eq(EventAuthenticationLogout.of(rndSession)));
        verify(this.incidentPublisher).publishAuthenticationLogoutFull(eq(IncidentAuthenticationLogoutFull.of(session1.getUsername(), dbUserSession1.getRequestMetadata())));
        verify(this.incidentPublisher).publishAuthenticationLogoutFull(eq(IncidentAuthenticationLogoutFull.of(session2.getUsername(), dbUserSession2.getRequestMetadata())));
        verify(this.incidentPublisher).publishAuthenticationLogoutFull(eq(IncidentAuthenticationLogoutFull.of(session3.getUsername(), dbUserSession3.getRequestMetadata())));
        verify(this.incidentPublisher).publishAuthenticationLogoutFull(eq(IncidentAuthenticationLogoutFull.of(session4.getUsername(), dbUserSession4.getRequestMetadata())));
        verify(this.incidentPublisher).publishAuthenticationLogoutFull(eq(IncidentAuthenticationLogoutFull.of(rndSession.getUsername(), rndDbUserSession.getRequestMetadata())));
        verify(this.userSessionService).deleteByRefreshToken(eq(session1.getRefreshToken()));
        verify(this.userSessionService).deleteByRefreshToken(eq(session2.getRefreshToken()));
        verify(this.userSessionService).deleteByRefreshToken(eq(session3.getRefreshToken()));
        verify(this.userSessionService).deleteByRefreshToken(eq(session4.getRefreshToken()));
        verify(this.userSessionService).deleteByRefreshToken(eq(rndSession.getRefreshToken()));
    }

    @Test
    public void registerTest() {
        // Arrange
        var username = Username.of("incident");

        // Act
        this.componentUnderTest.register(Session.of(username, entity(JwtRefreshToken.class)));
        this.componentUnderTest.register(Session.of(username, entity(JwtRefreshToken.class)));

        var duplicatedJwtRefreshToken = entity(JwtRefreshToken.class);
        this.componentUnderTest.register(Session.of(username, duplicatedJwtRefreshToken));
        this.componentUnderTest.register(Session.of(username, duplicatedJwtRefreshToken));
        this.componentUnderTest.register(Session.of(username, duplicatedJwtRefreshToken));

        // Assert
        assertThat(this.componentUnderTest.getActiveSessionsUsernamesIdentifiers()).hasSize(1);
        assertThat(this.componentUnderTest.getActiveSessionsUsernames()).hasSize(1);
        verify(this.securityJwtPublisher, times(3)).publishAuthenticationLogin(eq(EventAuthenticationLogin.of(username)));
    }

    @Test
    public void renewTest() {
        // Arrange
        var username = Username.of("incident");

        // Act
        this.componentUnderTest.renew(Session.of(username, entity(JwtRefreshToken.class)), Session.of(username, entity(JwtRefreshToken.class)));
        this.componentUnderTest.renew(Session.of(username, entity(JwtRefreshToken.class)), Session.of(username, entity(JwtRefreshToken.class)));

        var duplicatedJwtRefreshToken = entity(JwtRefreshToken.class);
        this.componentUnderTest.renew(Session.of(username, entity(JwtRefreshToken.class)), Session.of(username, duplicatedJwtRefreshToken));
        this.componentUnderTest.renew(Session.of(username, entity(JwtRefreshToken.class)), Session.of(username, duplicatedJwtRefreshToken));
        this.componentUnderTest.renew(Session.of(username, entity(JwtRefreshToken.class)), Session.of(username, duplicatedJwtRefreshToken));

        // Assert
        assertThat(this.componentUnderTest.getActiveSessionsUsernamesIdentifiers()).hasSize(1);
        assertThat(this.componentUnderTest.getActiveSessionsUsernames()).hasSize(1);
        verify(this.securityJwtPublisher, times(3)).publishSessionRefreshed(any(EventSessionRefreshed.class));
    }

    @Test
    public void logoutDbUserSessionPresentTest() {
        // Arrange
        var username = Username.of("incident");
        var refreshToken = entity(JwtRefreshToken.class);
        var session = Session.of(username, refreshToken);
        var dbUserSession = entity(DbUserSession.class);
        when(this.userSessionService.findByRefreshToken(eq(refreshToken))).thenReturn(dbUserSession);

        // Act
        this.componentUnderTest.logout(session);

        // Assert
        verify(this.userSessionService).findByRefreshToken(eq(refreshToken));
        var eventAC = ArgumentCaptor.forClass(EventAuthenticationLogout.class);
        verify(this.securityJwtPublisher).publishAuthenticationLogout(eventAC.capture());
        verify(this.securityJwtPublisher).publishAuthenticationLogout(eventAC.capture());
        var incidentAC = ArgumentCaptor.forClass(IncidentAuthenticationLogoutFull.class);
        verify(this.incidentPublisher).publishAuthenticationLogoutFull(incidentAC.capture());
        var incident = incidentAC.getValue();
        assertThat(incident.getUsername()).isEqualTo(username);
        assertThat(incident.getUserRequestMetadata()).isEqualTo(dbUserSession.getRequestMetadata());
        verify(this.userSessionService).deleteByRefreshToken(eq(refreshToken));
    }

    @Test
    public void logoutDbUserSessionNotPresentTest() {
        // Arrange
        var username = Username.of("incident");
        var refreshToken = entity(JwtRefreshToken.class);
        var session = Session.of(username, refreshToken);
        when(this.userSessionService.findByRefreshToken(eq(refreshToken))).thenReturn(null);

        // Act
        this.componentUnderTest.logout(session);

        // Assert
        verify(this.userSessionService).findByRefreshToken(eq(refreshToken));
        var eventAC = ArgumentCaptor.forClass(EventAuthenticationLogout.class);
        verify(this.securityJwtPublisher).publishAuthenticationLogout(eventAC.capture());
        assertThat(eventAC.getValue().getSession()).isEqualTo(session);
        var incidentAC = ArgumentCaptor.forClass(IncidentAuthenticationLogoutMin.class);
        verify(this.incidentPublisher).publishAuthenticationLogoutMin(incidentAC.capture());
        assertThat(incidentAC.getValue().getUsername()).isEqualTo(username);
    }

    @Test
    public void cleanByExpiredRefreshTokensEnabledTest() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        var username1 = Username.of("username1");
        var session1 = Session.of(username1, entity(JwtRefreshToken.class));
        var session2 = Session.of(Username.of("username2"), entity(JwtRefreshToken.class));
        var session3 = Session.of(Username.of("username3"), entity(JwtRefreshToken.class));
        Set<Session> sessions = ConcurrentHashMap.newKeySet();
        sessions.add(session1);
        sessions.add(session2);
        sessions.add(session3);
        setPrivateField(this.componentUnderTest, "sessions", sessions);
        var dbUserSession1 = mock(DbUserSession.class);
        when(dbUserSession1.getId()).thenReturn(randomString());
        when(dbUserSession1.getRequestMetadata()).thenReturn(entity(UserRequestMetadata.class));
        var dbUserSession2 = mock(DbUserSession.class);
        when(dbUserSession2.getId()).thenReturn(randomString());
        when(dbUserSession2.getRequestMetadata()).thenReturn(entity(UserRequestMetadata.class));
        var dbUserSession3 = mock(DbUserSession.class);
        when(dbUserSession3.getId()).thenReturn(randomString());
        when(dbUserSession3.getRequestMetadata()).thenReturn(entity(UserRequestMetadata.class));
        var usersSessions = List.of(dbUserSession1, dbUserSession2, dbUserSession3);
        var sessionsValidatedTuple2 = SessionsValidatedTuple2.of(List.of(Tuple2.of(username1, dbUserSession3)), List.of(dbUserSession1.getId(), dbUserSession2.getId()));
        when(this.userSessionService.validate(eq(usersSessions))).thenReturn(sessionsValidatedTuple2);

        // Act
        this.componentUnderTest.cleanByExpiredRefreshTokens(usersSessions);

        // Assert
        verify(this.userSessionService).validate(eq(usersSessions));
        assertThat(this.componentUnderTest.getActiveSessionsUsernamesIdentifiers()).hasSize(3);
        assertThat(this.componentUnderTest.getActiveSessionsUsernames()).hasSize(3);
        var eseCaptor = ArgumentCaptor.forClass(EventSessionExpired.class);
        verify(this.securityJwtPublisher).publishSessionExpired(eseCaptor.capture());
        var eventSessionExpired = eseCaptor.getValue();
        assertThat(eventSessionExpired.getSession().getUsername()).isEqualTo(username1);
        assertThat(eventSessionExpired.getSession().getRefreshToken()).isEqualTo(dbUserSession3.getJwtRefreshToken());
        var seiCaptor = ArgumentCaptor.forClass(IncidentSessionExpired.class);
        verify(this.incidentPublisher).publishSessionExpired(seiCaptor.capture());
        var sessionExpiredIncident = seiCaptor.getValue();
        assertThat(sessionExpiredIncident.getUsername()).isEqualTo(username1);
        assertThat(sessionExpiredIncident.getUserRequestMetadata()).isEqualTo(dbUserSession3.getRequestMetadata());
        verify(this.userSessionService).deleteByIdIn(List.of(dbUserSession1.getId(), dbUserSession2.getId()));
    }
}
