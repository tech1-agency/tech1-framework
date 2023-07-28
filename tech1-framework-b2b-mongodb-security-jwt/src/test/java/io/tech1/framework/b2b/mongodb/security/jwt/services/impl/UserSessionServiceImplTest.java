package io.tech1.framework.b2b.mongodb.security.jwt.services.impl;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.utilities.SecurityJwtTokenUtility;
import io.tech1.framework.b2b.base.security.jwt.utilities.impl.SecurityJwtTokenUtilityImpl;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUserSession;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.EventSessionAddUserRequestMetadata;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.mongodb.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.UserSessionRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.services.UserSessionService;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.constants.StringConstants;
import io.tech1.framework.domain.enums.Status;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.properties.ApplicationFrameworkProperties;
import io.tech1.framework.properties.tests.contexts.ApplicationFrameworkPropertiesContext;
import io.tech1.framework.utilities.browsers.UserAgentDetailsUtility;
import io.tech1.framework.utilities.browsers.impl.UserAgentDetailsUtilityImpl;
import io.tech1.framework.utilities.geo.facades.GeoLocationFacadeUtility;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.constants.StringConstants.UNDEFINED;
import static io.tech1.framework.domain.utilities.random.EntityUtility.*;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class UserSessionServiceImplTest {

    @Configuration
    @Import({
            ApplicationFrameworkPropertiesContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        // Properties
        private final ApplicationFrameworkProperties applicationFrameworkProperties;

        @Bean
        SecurityJwtPublisher securityJwtPublisher() {
            return mock(SecurityJwtPublisher.class);
        }

        @Bean
        UserSessionRepository userSessionRepository() {
            return mock(UserSessionRepository.class);
        }

        @Bean
        GeoLocationFacadeUtility geoLocationFacadeUtility() {
            return mock(GeoLocationFacadeUtility.class);
        }

        @Bean
        public SecurityJwtTokenUtility securityJwtTokenUtility() {
            return new SecurityJwtTokenUtilityImpl(
                    this.applicationFrameworkProperties
            );
        }

        @Bean
        UserAgentDetailsUtility userAgentDetailsUtility() {
            return new UserAgentDetailsUtilityImpl();
        }

        @Bean
        UserSessionService userSessionService() {
            return new UserSessionServiceImpl(
                    this.securityJwtPublisher(),
                    this.userSessionRepository(),
                    this.geoLocationFacadeUtility(),
                    this.securityJwtTokenUtility(),
                    this.userAgentDetailsUtility()
            );
        }
    }

    // Publishers
    private final SecurityJwtPublisher securityJwtPublisher;
    // Repositories
    private final UserSessionRepository userSessionRepository;
    // Utilities
    private final GeoLocationFacadeUtility geoLocationFacadeUtility;
    private final UserAgentDetailsUtility userAgentDetailsUtility;

    private final UserSessionService componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.securityJwtPublisher,
                this.userSessionRepository,
                this.geoLocationFacadeUtility
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.securityJwtPublisher,
                this.userSessionRepository,
                this.geoLocationFacadeUtility
        );
    }

    @Test
    void findByUsernameTest() {
        // Arrange
        var username = randomUsername();
        var usersSessions = list345(DbUserSession.class);
        when(this.userSessionRepository.findByUsername(username)).thenReturn(usersSessions);

        // Act
        var actual = this.componentUnderTest.findByUsername(username);

        // Assert
        verify(this.userSessionRepository).findByUsername(username);
        assertThat(actual).isEqualTo(usersSessions);
    }

    @Test
    void findByUsernameInTest() {
        // Arrange
        var usernames = set345(Username.class);
        var usersSessions = list345(DbUserSession.class);
        when(this.userSessionRepository.findByUsernameIn(usernames)).thenReturn(usersSessions);

        // Act
        var actual = this.componentUnderTest.findByUsernameIn(usernames);

        // Assert
        verify(this.userSessionRepository).findByUsernameIn(usernames);
        assertThat(actual).isEqualTo(usersSessions);
    }

    @Test
    void deleteByIdInTest() {
        // Arrange
        var ids = randomStringsAsList(3);
        var deletedRecords = randomLongGreaterThanZero();
        when(this.userSessionRepository.deleteByIdIn(ids)).thenReturn(deletedRecords);

        // Act
        var actual = this.componentUnderTest.deleteByIdIn(ids);

        // Assert
        verify(this.userSessionRepository).deleteByIdIn(ids);
        assertThat(actual).isEqualTo(deletedRecords);
    }

    @Test
    void findByRefreshTokenTest() {
        // Arrange
        var jwtRefreshToken = entity(JwtRefreshToken.class);
        var userSession = entity(DbUserSession.class);
        when(this.userSessionRepository.findByRefreshToken(jwtRefreshToken)).thenReturn(userSession);

        // Act
        var actual = this.componentUnderTest.findByRefreshToken(jwtRefreshToken);

        // Assert
        verify(this.userSessionRepository).findByRefreshToken(jwtRefreshToken);
        assertThat(actual).isEqualTo(userSession);
    }

    @Test
    void deleteByRefreshTokenTest() {
        // Arrange
        var jwtRefreshToken = entity(JwtRefreshToken.class);

        // Act
        this.componentUnderTest.deleteByRefreshToken(jwtRefreshToken);

        // Assert
        verify(this.userSessionRepository).deleteByRefreshToken(jwtRefreshToken);
    }

    @Test
    void saveUserSessionNotNullTest() {
        // Arrange
        var ipAddr = randomIPv4();
        var httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getHeader("User-Agent")).thenReturn(randomString());
        when(httpServletRequest.getHeader("X-Forwarded-For")).thenReturn(ipAddr);
        var jwtUser = entity(JwtUser.class);
        var username = jwtUser.username();
        var jwtRefreshToken = entity(JwtRefreshToken.class);
        var userSession = new DbUserSession(jwtRefreshToken, username, entity(UserRequestMetadata.class));
        var savedUserSession = entity(DbUserSession.class);
        when(this.userSessionRepository.findByRefreshToken(jwtRefreshToken)).thenReturn(userSession);
        when(this.userSessionRepository.save(any())).thenReturn(savedUserSession);

        // Act
        this.componentUnderTest.save(jwtUser, jwtRefreshToken, httpServletRequest);

        // Assert
        verify(this.userSessionRepository).findByRefreshToken(jwtRefreshToken);
        var dbUserSessionAC = ArgumentCaptor.forClass(DbUserSession.class);
        verify(this.userSessionRepository).save(dbUserSessionAC.capture());
        var actualDbUserSession = dbUserSessionAC.getValue();
        assertThat(actualDbUserSession.getUsername()).isEqualTo(username);
        assertThat(actualDbUserSession.getJwtRefreshToken()).isEqualTo(jwtRefreshToken);
        var requestMetadata = actualDbUserSession.getRequestMetadata();
        assertThat(requestMetadata.getStatus()).isEqualTo(Status.STARTED);
        assertThat(requestMetadata.getGeoLocation().getIpAddr()).isEqualTo(ipAddr);
        var whereTuple3 = requestMetadata.getWhereTuple3();
        assertThat(whereTuple3.a()).isEqualTo(ipAddr);
        assertThat(whereTuple3.b()).isEqualTo(StringConstants.NO_FLAG);
        assertThat(whereTuple3.c()).isEqualTo("Processing...Please wait!");
        var whatTuple2 = requestMetadata.getWhatTuple2();
        assertThat(whatTuple2.a()).isEqualTo(UNDEFINED);
        assertThat(whatTuple2.b()).isEqualTo("—");
        assertThat(actualDbUserSession.getId()).isNotNull();
        var eventAC = ArgumentCaptor.forClass(EventSessionAddUserRequestMetadata.class);
        verify(this.securityJwtPublisher).publishSessionAddUserRequestMetadata(eventAC.capture());
        var event = eventAC.getValue();
        assertThat(event.username()).isEqualTo(username);
        assertThat(event.userSession()).isEqualTo(savedUserSession);
        assertThat(event.isAuthenticationLoginEndpoint()).isTrue();
        assertThat(event.isAuthenticationRefreshTokenEndpoint()).isFalse();
    }

    @Test
    void saveUserSessionNullTest() {
        // Arrange
        var ipAddr = randomIPv4();
        var httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getHeader("User-Agent")).thenReturn(randomString());
        when(httpServletRequest.getHeader("X-Forwarded-For")).thenReturn(ipAddr);
        var jwtUser = entity(JwtUser.class);
        var username = jwtUser.username();
        var jwtRefreshToken = entity(JwtRefreshToken.class);
        var savedUserSession = entity(DbUserSession.class);
        when(this.userSessionRepository.save(any())).thenReturn(savedUserSession);

        // Act
        this.componentUnderTest.save(jwtUser, jwtRefreshToken, httpServletRequest);

        // Assert
        verify(this.userSessionRepository).findByRefreshToken(jwtRefreshToken);
        var dbUserSessionAC = ArgumentCaptor.forClass(DbUserSession.class);
        verify(this.userSessionRepository).save(dbUserSessionAC.capture());
        var actualDbUserSession = dbUserSessionAC.getValue();
        assertThat(actualDbUserSession.getUsername()).isEqualTo(username);
        assertThat(actualDbUserSession.getJwtRefreshToken()).isEqualTo(jwtRefreshToken);
        var requestMetadata = actualDbUserSession.getRequestMetadata();
        assertThat(requestMetadata.getStatus()).isEqualTo(Status.STARTED);
        assertThat(requestMetadata.getGeoLocation().getIpAddr()).isEqualTo(ipAddr);
        var whereTuple3 = requestMetadata.getWhereTuple3();
        assertThat(whereTuple3.a()).isEqualTo(ipAddr);
        assertThat(whereTuple3.b()).isEqualTo(StringConstants.NO_FLAG);
        assertThat(whereTuple3.c()).isEqualTo("Processing...Please wait!");
        var whatTuple2 = requestMetadata.getWhatTuple2();
        assertThat(whatTuple2.a()).isEqualTo(UNDEFINED);
        assertThat(whatTuple2.b()).isEqualTo("—");
        assertThat(actualDbUserSession.getId()).isNotNull();
        var eventAC = ArgumentCaptor.forClass(EventSessionAddUserRequestMetadata.class);
        verify(this.securityJwtPublisher).publishSessionAddUserRequestMetadata(eventAC.capture());
        var event = eventAC.getValue();
        assertThat(event.username()).isEqualTo(username);
        assertThat(event.userSession()).isEqualTo(savedUserSession);
        assertThat(event.isAuthenticationLoginEndpoint()).isTrue();
        assertThat(event.isAuthenticationRefreshTokenEndpoint()).isFalse();
    }

    @Test
    void refreshTest() {
        // Arrange
        var httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getHeader("User-Agent")).thenReturn(randomString());
        var jwtUser = entity(JwtUser.class);
        var username = jwtUser.username();
        var oldJwtRefreshToken = entity(JwtRefreshToken.class);
        var newJwtRefreshToken = entity(JwtRefreshToken.class);
        var oldUserSession = new DbUserSession(oldJwtRefreshToken, randomUsername(), entity(UserRequestMetadata.class));
        when(this.userSessionRepository.findByRefreshToken(oldJwtRefreshToken)).thenReturn(oldUserSession);

        // Act
        var dbUserSession = this.componentUnderTest.refresh(jwtUser, oldJwtRefreshToken, newJwtRefreshToken, httpServletRequest);

        // Assert
        verify(this.userSessionRepository).findByRefreshToken(oldJwtRefreshToken);
        var saveCaptor = ArgumentCaptor.forClass(DbUserSession.class);
        verify(this.userSessionRepository).save(saveCaptor.capture());
        var newUserSession = saveCaptor.getValue();
        assertThat(newUserSession.getUsername()).isEqualTo(username);
        assertThat(newUserSession.getJwtRefreshToken()).isEqualTo(newJwtRefreshToken);
        assertThat(newUserSession.getRequestMetadata()).isEqualTo(oldUserSession.getRequestMetadata());
        verify(this.userSessionRepository).delete(oldUserSession);
        var eventAC = ArgumentCaptor.forClass(EventSessionAddUserRequestMetadata.class);
        verify(this.securityJwtPublisher).publishSessionAddUserRequestMetadata(eventAC.capture());
        var event = eventAC.getValue();
        assertThat(event.username()).isEqualTo(username);
        assertThat(event.userSession()).isEqualTo(newUserSession);
        assertThat(event.isAuthenticationLoginEndpoint()).isFalse();
        assertThat(event.isAuthenticationRefreshTokenEndpoint()).isTrue();
        assertThat(dbUserSession).isEqualTo(newUserSession);
    }

    @Test
    void saveUserRequestMetadataTest() {
        // Arrange
        var event = entity(EventSessionAddUserRequestMetadata.class);
        var geoLocation = randomGeoLocation();
        when(this.geoLocationFacadeUtility.getGeoLocation(event.clientIpAddr())).thenReturn(geoLocation);
        var userSessionAC = ArgumentCaptor.forClass(DbUserSession.class);

        // Act
        this.componentUnderTest.saveUserRequestMetadata(event);

        // Assert
        verify(this.geoLocationFacadeUtility).getGeoLocation(event.clientIpAddr());
        verify(this.userSessionRepository).save(userSessionAC.capture());
        var userSession = userSessionAC.getValue();
        assertThat(userSession.getRequestMetadata().getGeoLocation()).isEqualTo(geoLocation);
        assertThat(userSession.getRequestMetadata().getUserAgentDetails()).isEqualTo(this.userAgentDetailsUtility.getUserAgentDetails(event.userAgentHeader()));
    }

    @Test
    void validateTest() {
        // Arrange
        var sessionInvalidUserSession = new DbUserSession(
                new JwtRefreshToken("<invalid>"),
                randomUsername(),
                entity(UserRequestMetadata.class)
        );
        var sessionExpiredUserSession = new DbUserSession(
                new JwtRefreshToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdWx0aXVzZXI0MyIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJhZG1pbiJ9LHsiYXV0aG9yaXR5IjoidXNlciJ9XSwiaWF0IjoxNjQyNzc0NTk3LCJleHAiOjE2NDI3NzQ2Mjd9.aCeKIy8uvei_c_aXoHlVhQ1N8wmjfguXgi2fWMRYVp8"),
                randomUsername(),
                entity(UserRequestMetadata.class)
        );
        var sessionAliveUserSession = new DbUserSession(
                new JwtRefreshToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdWx0aXVzZXI0MyIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJhZG1pbiJ9LHsiYXV0aG9yaXR5IjoidXNlciJ9XSwiaWF0IjoxNjQyNzc0Nzc4LCJleHAiOjQ3OTg0NDgzNzh9._BMUZR3wls5O1BYDm_4loYi3vn70GjE39Cpuqh-Z_bY"),
                randomUsername(),
                entity(UserRequestMetadata.class)
        );
        var usersSessions = List.of(sessionInvalidUserSession, sessionExpiredUserSession, sessionAliveUserSession);

        // Act
        var sessionsValidatedTuple2 = this.componentUnderTest.validate(usersSessions);

        // Assert
        assertThat(sessionsValidatedTuple2).isNotNull();
        assertThat(sessionsValidatedTuple2.expiredOrInvalidSessionIds()).isNotNull();
        assertThat(sessionsValidatedTuple2.expiredOrInvalidSessionIds()).hasSize(2);
        assertThat(sessionsValidatedTuple2.expiredOrInvalidSessionIds()).containsExactlyInAnyOrder(
                sessionInvalidUserSession.getId(),
                sessionExpiredUserSession.getId()
        );
        assertThat(sessionsValidatedTuple2.expiredSessions()).isNotNull();
        assertThat(sessionsValidatedTuple2.expiredSessions()).hasSize(1);
        assertThat(sessionsValidatedTuple2.expiredSessions().get(0).a().identifier()).isEqualTo("multiuser43");
    }

    @Test
    void deleteByIdTest() {
        // Arrange
        var sessionId = randomString();

        // Act
        this.componentUnderTest.deleteById(sessionId);

        // Assert
        verify(this.userSessionRepository).deleteById(sessionId);
    }

    @SuppressWarnings("unchecked")
    @Test
    void deleteAllExceptCurrentTest() {
        // Arrange
        var username = entity(Username.class);
        var cookie = entity(CookieRefreshToken.class);
        var session1 = new DbUserSession(new JwtRefreshToken("session1"), randomUsername(), entity(UserRequestMetadata.class));
        var session2 = new DbUserSession(new JwtRefreshToken("session2"), randomUsername(), entity(UserRequestMetadata.class));
        var session3 = new DbUserSession(new JwtRefreshToken("session3"), randomUsername(), entity(UserRequestMetadata.class));
        var session4 = new DbUserSession(new JwtRefreshToken("session4"), randomUsername(), entity(UserRequestMetadata.class));
        var sessions = new ArrayList<>(List.of(session1, session2, session3, session4));
        when(this.userSessionRepository.findByUsername(username)).thenReturn(sessions);
        when(this.userSessionRepository.findByRefreshToken(cookie.getJwtRefreshToken())).thenReturn(session3);

        // Act
        this.componentUnderTest.deleteAllExceptCurrent(username, cookie);

        // Assert
        verify(this.userSessionRepository).findByUsername(username);
        verify(this.userSessionRepository).findByRefreshToken(cookie.getJwtRefreshToken());
        var sessionsAC = ArgumentCaptor.forClass(List.class);
        verify(this.userSessionRepository).deleteAll(sessionsAC.capture());
        assertThat(sessionsAC.getValue()).hasSize(3);
        assertThat(((List<DbUserSession>) sessionsAC.getValue()).stream().map(DbUserSession::getId).collect(Collectors.toSet())).containsExactlyInAnyOrder(
                "session1",
                "session2",
                "session4"
        );
    }

    @SuppressWarnings("unchecked")
    @Test
    void deleteAllExceptCurrentAsSuperuserTest() {
        // Arrange
        var cookie = entity(CookieRefreshToken.class);
        var session1 = new DbUserSession(new JwtRefreshToken("session1"), randomUsername(), entity(UserRequestMetadata.class));
        var session2 = new DbUserSession(new JwtRefreshToken("session2"), randomUsername(), entity(UserRequestMetadata.class));
        var session3 = new DbUserSession(new JwtRefreshToken("session3"), randomUsername(), entity(UserRequestMetadata.class));
        var session4 = new DbUserSession(new JwtRefreshToken("session4"), randomUsername(), entity(UserRequestMetadata.class));
        var sessions = new ArrayList<>(List.of(session1, session2, session3, session4));
        when(this.userSessionRepository.findAll()).thenReturn(sessions);
        when(this.userSessionRepository.findByRefreshToken(cookie.getJwtRefreshToken())).thenReturn(session3);

        // Act
        this.componentUnderTest.deleteAllExceptCurrentAsSuperuser(cookie);

        // Assert
        verify(this.userSessionRepository).findAll();
        verify(this.userSessionRepository).findByRefreshToken(cookie.getJwtRefreshToken());
        var sessionsAC = ArgumentCaptor.forClass(List.class);
        verify(this.userSessionRepository).deleteAll(sessionsAC.capture());
        assertThat(sessionsAC.getValue()).hasSize(3);
        assertThat(((List<DbUserSession>) sessionsAC.getValue()).stream().map(DbUserSession::getId).collect(Collectors.toSet())).containsExactlyInAnyOrder(
                "session1",
                "session2",
                "session4"
        );
    }
}
