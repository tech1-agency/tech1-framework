package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.db.UserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.events.EventSessionUserRequestMetadataAdd;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.base.security.jwt.repositories.UsersSessionsRepository;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityJwtTokenUtils;
import io.tech1.framework.b2b.base.security.jwt.utils.impl.SecurityJwtTokenUtilsImpl;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.enums.Status;
import io.tech1.framework.domain.tuples.TuplePresence;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.tech1.framework.b2b.base.security.jwt.tests.random.BaseSecurityJwtDbRandomUtility.session;
import static io.tech1.framework.b2b.base.security.jwt.tests.random.BaseSecurityJwtRandomUtility.randomPersistedSession;
import static io.tech1.framework.domain.constants.StringConstants.UNDEFINED;
import static io.tech1.framework.domain.tests.constants.TestsFlagsConstants.FLAG_UNKNOWN;
import static io.tech1.framework.domain.tests.constants.TestsUsernamesConstants.TECH1;
import static io.tech1.framework.domain.tuples.TuplePresence.present;
import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AbstractBaseUsersSessionsServiceTest {

    @Configuration
    @Import({
            ApplicationFrameworkPropertiesContext.class
    })
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    static class ContextConfiguration {
        private final ApplicationFrameworkProperties applicationFrameworkProperties;

        @Bean
        SecurityJwtPublisher securityJwtPublisher() {
            return mock(SecurityJwtPublisher.class);
        }

        @Bean
        UsersSessionsRepository usersSessionsRepository() {
            return mock(UsersSessionsRepository.class);
        }

        @Bean
        GeoLocationFacadeUtility geoLocationFacadeUtility() {
            return mock(GeoLocationFacadeUtility.class);
        }

        @Bean
        public SecurityJwtTokenUtils securityJwtTokenUtils() {
            return new SecurityJwtTokenUtilsImpl(
                    this.applicationFrameworkProperties
            );
        }

        @Bean
        UserAgentDetailsUtility userAgentDetailsUtility() {
            return new UserAgentDetailsUtilityImpl();
        }

        @Bean
        AbstractBaseUsersSessionsService abstractTokensContextThrowerService() {
            return new AbstractBaseUsersSessionsService(
                    this.securityJwtPublisher(),
                    this.usersSessionsRepository(),
                    this.geoLocationFacadeUtility(),
                    this.securityJwtTokenUtils(),
                    this.userAgentDetailsUtility()
            ) {};
        }
    }

    // Publishers
    protected final SecurityJwtPublisher securityJwtPublisher;
    // Repositories
    protected final UsersSessionsRepository usersSessionsRepository;
    // Utilities
    protected final GeoLocationFacadeUtility geoLocationFacadeUtility;
    protected final SecurityJwtTokenUtils securityJwtTokenUtils;
    protected final UserAgentDetailsUtility userAgentDetailsUtility;

    private final AbstractBaseUsersSessionsService componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.securityJwtPublisher,
                this.usersSessionsRepository,
                this.geoLocationFacadeUtility
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.securityJwtPublisher,
                this.usersSessionsRepository,
                this.geoLocationFacadeUtility
        );
    }

    @Test
    void saveUserSessionNotNullTest() {
        // Arrange
        var ipAddr = randomIPv4();
        var httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getHeader("User-Agent")).thenReturn(randomString());
        when(httpServletRequest.getHeader("X-Forwarded-For")).thenReturn(ipAddr);
        var user = entity(JwtUser.class);
        var username = user.username();
        var accessToken = entity(JwtAccessToken.class);
        var refreshToken = entity(JwtRefreshToken.class);
        var userSession = session(username, accessToken, refreshToken);
        when(this.usersSessionsRepository.isPresent(accessToken)).thenReturn(present(userSession));
        when(this.usersSessionsRepository.saveAs(any(UserSession.class))).thenReturn(userSession);

        // Act
        this.componentUnderTest.save(user, accessToken, refreshToken, httpServletRequest);

        // Assert
        verify(this.usersSessionsRepository).isPresent(accessToken);
        var dbUserSessionAC = ArgumentCaptor.forClass(UserSession.class);
        verify(this.usersSessionsRepository).saveAs(dbUserSessionAC.capture());
        var actualDbUserSession = dbUserSessionAC.getValue();
        assertThat(actualDbUserSession.username()).isEqualTo(username);
        assertThat(actualDbUserSession.refreshToken()).isEqualTo(refreshToken);
        var requestMetadata = actualDbUserSession.metadata();
        assertThat(requestMetadata.getStatus()).isEqualTo(Status.STARTED);
        assertThat(requestMetadata.getGeoLocation().getIpAddr()).isEqualTo(ipAddr);
        var whereTuple3 = requestMetadata.getWhereTuple3();
        assertThat(whereTuple3.a()).isEqualTo(ipAddr);
        assertThat(whereTuple3.b()).isEqualTo(FLAG_UNKNOWN);
        assertThat(whereTuple3.c()).isEqualTo("Processing...Please wait!");
        var whatTuple2 = requestMetadata.getWhatTuple2();
        assertThat(whatTuple2.a()).isEqualTo(UNDEFINED);
        assertThat(whatTuple2.b()).isEqualTo("—");
        assertThat(actualDbUserSession.id()).isNotNull();
        var eventAC = ArgumentCaptor.forClass(EventSessionUserRequestMetadataAdd.class);
        verify(this.securityJwtPublisher).publishSessionAddUserRequestMetadata(eventAC.capture());
        var event = eventAC.getValue();
        assertThat(event.username()).isEqualTo(username);
        assertThat(event.session().id()).isEqualTo(actualDbUserSession.id());
        assertThat(event.session().metadata()).isNotEqualTo(actualDbUserSession.metadata());
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
        var user = entity(JwtUser.class);
        var username = user.username();
        var accessToken = entity(JwtAccessToken.class);
        var refreshToken = entity(JwtRefreshToken.class);
        when(this.usersSessionsRepository.isPresent(accessToken)).thenReturn(TuplePresence.absent());
        var userSession = session(username, accessToken, refreshToken);
        when(this.usersSessionsRepository.saveAs(any(UserSession.class))).thenReturn(userSession);

        // Act
        this.componentUnderTest.save(user, accessToken, refreshToken, httpServletRequest);

        // Assert
        verify(this.usersSessionsRepository).isPresent(accessToken);
        var dbUserSessionAC = ArgumentCaptor.forClass(UserSession.class);
        verify(this.usersSessionsRepository).saveAs(dbUserSessionAC.capture());
        var actualDbUserSession = dbUserSessionAC.getValue();
        assertThat(actualDbUserSession.username()).isEqualTo(username);
        assertThat(actualDbUserSession.refreshToken()).isEqualTo(refreshToken);
        var requestMetadata = actualDbUserSession.metadata();
        assertThat(requestMetadata.getStatus()).isEqualTo(Status.STARTED);
        assertThat(requestMetadata.getGeoLocation().getIpAddr()).isEqualTo(ipAddr);
        var whereTuple3 = requestMetadata.getWhereTuple3();
        assertThat(whereTuple3.a()).isEqualTo(ipAddr);
        assertThat(whereTuple3.b()).isEqualTo(FLAG_UNKNOWN);
        assertThat(whereTuple3.c()).isEqualTo("Processing...Please wait!");
        var whatTuple2 = requestMetadata.getWhatTuple2();
        assertThat(whatTuple2.a()).isEqualTo(UNDEFINED);
        assertThat(whatTuple2.b()).isEqualTo("—");
        assertThat(actualDbUserSession.id()).isNotNull();
        var eventAC = ArgumentCaptor.forClass(EventSessionUserRequestMetadataAdd.class);
        verify(this.securityJwtPublisher).publishSessionAddUserRequestMetadata(eventAC.capture());
        var event = eventAC.getValue();
        assertThat(event.username()).isEqualTo(username);
        assertThat(event.session().id()).isNotEqualTo(actualDbUserSession.id());
        assertThat(event.session().metadata()).isNotEqualTo(actualDbUserSession.metadata());
        assertThat(event.isAuthenticationLoginEndpoint()).isTrue();
        assertThat(event.isAuthenticationRefreshTokenEndpoint()).isFalse();
    }

    @Test
    void refreshTest() {
        // Arrange
        var httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getHeader("User-Agent")).thenReturn(randomString());
        var user = entity(JwtUser.class);
        var username = user.username();
        var newAccessToken = entity(JwtAccessToken.class);
        var newRefreshToken = entity(JwtRefreshToken.class);
        var oldSession = randomPersistedSession();
        when(this.usersSessionsRepository.saveAs(any(UserSession.class))).thenReturn(randomPersistedSession());

        // Act
        this.componentUnderTest.refresh(user, oldSession, newAccessToken, newRefreshToken, httpServletRequest);

        // Assert
        var saveCaptor = ArgumentCaptor.forClass(UserSession.class);
        verify(this.usersSessionsRepository).saveAs(saveCaptor.capture());
        var newUserSession = saveCaptor.getValue();
        assertThat(newUserSession.username()).isEqualTo(username);
        assertThat(newUserSession.refreshToken()).isEqualTo(newRefreshToken);
        assertThat(newUserSession.metadata()).isEqualTo(oldSession.metadata());
        verify(this.usersSessionsRepository).delete(oldSession.id());
        var eventAC = ArgumentCaptor.forClass(EventSessionUserRequestMetadataAdd.class);
        verify(this.securityJwtPublisher).publishSessionAddUserRequestMetadata(eventAC.capture());
        var event = eventAC.getValue();
        assertThat(event.username()).isEqualTo(username);
        assertThat(event.email()).isEqualTo(user.email());
        assertThat(event.session().id()).isNotEqualTo(newUserSession.id());
        assertThat(event.isAuthenticationLoginEndpoint()).isFalse();
        assertThat(event.isAuthenticationRefreshTokenEndpoint()).isTrue();
    }

    @Test
    void saveUserRequestMetadataTest() {
        // Arrange
        var event = entity(EventSessionUserRequestMetadataAdd.class);
        var geoLocation = randomGeoLocation();
        when(this.geoLocationFacadeUtility.getGeoLocation(event.clientIpAddr())).thenReturn(geoLocation);
        var userSessionAC = ArgumentCaptor.forClass(UserSession.class);
        when(this.usersSessionsRepository.saveAs(any(UserSession.class))).thenReturn(event.session());

        // Act
        this.componentUnderTest.saveUserRequestMetadata(event);

        // Assert
        verify(this.geoLocationFacadeUtility).getGeoLocation(event.clientIpAddr());
        verify(this.usersSessionsRepository).saveAs(userSessionAC.capture());
        var requestMetadata = userSessionAC.getValue().metadata();
        assertThat(requestMetadata.getGeoLocation()).isEqualTo(geoLocation);
        assertThat(requestMetadata.getUserAgentDetails()).isEqualTo(this.userAgentDetailsUtility.getUserAgentDetails(event.userAgentHeader()));
    }

    @Test
    void getExpiredSessionsTest() {
        // Arrange
        var usernames = new HashSet<>(Set.of(TECH1));
        var sessionInvalidUserSession = session(
                randomUsername(),
                entity(JwtAccessToken.class),
                new JwtRefreshToken("<invalid>")
        );
        var sessionExpiredUserSession = session(
                randomUsername(),
                entity(JwtAccessToken.class),
                new JwtRefreshToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdWx0aXVzZXI0MyIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJhZG1pbiJ9LHsiYXV0aG9yaXR5IjoidXNlciJ9XSwiaWF0IjoxNjQyNzc0NTk3LCJleHAiOjE2NDI3NzQ2Mjd9.aCeKIy8uvei_c_aXoHlVhQ1N8wmjfguXgi2fWMRYVp8")
        );
        var sessionAliveUserSession = session(
                randomUsername(),
                entity(JwtAccessToken.class),
                new JwtRefreshToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdWx0aXVzZXI0MyIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJhZG1pbiJ9LHsiYXV0aG9yaXR5IjoidXNlciJ9XSwiaWF0IjoxNjQyNzc0Nzc4LCJleHAiOjQ3OTg0NDgzNzh9._BMUZR3wls5O1BYDm_4loYi3vn70GjE39Cpuqh-Z_bY")
        );
        var usersSessions = List.of(sessionInvalidUserSession, sessionExpiredUserSession, sessionAliveUserSession);
        when(this.usersSessionsRepository.findByUsernameInAsAny(usernames)).thenReturn(usersSessions);

        // Act
        var sessionsValidatedTuple2 = this.componentUnderTest.getExpiredRefreshTokensSessions(usernames);

        // Assert
        verify(this.usersSessionsRepository).findByUsernameInAsAny(usernames);
        assertThat(sessionsValidatedTuple2).isNotNull();
        assertThat(sessionsValidatedTuple2.expiredOrInvalidSessionIds()).isNotNull();
        assertThat(sessionsValidatedTuple2.expiredOrInvalidSessionIds()).hasSize(2);
        assertThat(sessionsValidatedTuple2.expiredOrInvalidSessionIds()).containsExactlyInAnyOrder(
                sessionInvalidUserSession.id(),
                sessionExpiredUserSession.id()
        );
        assertThat(sessionsValidatedTuple2.expiredSessions()).isNotNull();
        assertThat(sessionsValidatedTuple2.expiredSessions()).hasSize(1);
        assertThat(sessionsValidatedTuple2.expiredSessions().get(0).a().identifier()).isEqualTo("multiuser43");
    }

    @Test
    void enableMetadataRenewCronTest() {
        // Act
        this.componentUnderTest.enableMetadataRenewCron();

        // Assert
        verify(this.usersSessionsRepository).enableMetadataRenewCron();
    }

    @Test
    void enableMetadataRenewManuallyTest() {
        // Arrange
        var sessionId = entity(UserSessionId.class);

        // Act
        this.componentUnderTest.enableMetadataRenewManually(sessionId);

        // Assert
        verify(this.usersSessionsRepository).enableMetadataRenewManually(sessionId);
    }

    @Test
    void deleteByIdTest() {
        // Arrange
        var sessionId = entity(UserSessionId.class);

        // Act
        this.componentUnderTest.deleteById(sessionId);

        // Assert
        verify(this.usersSessionsRepository).delete(sessionId);
    }

    @Test
    void deleteAllExceptCurrentTest() {
        // Arrange
        var username = entity(Username.class);
        var cookie = entity(CookieAccessToken.class);

        // Act
        this.componentUnderTest.deleteAllExceptCurrent(username, cookie);

        // Assert
        verify(this.usersSessionsRepository).deleteByUsernameExceptAccessToken(username, cookie);
    }

    @Test
    void deleteAllExceptCurrentAsSuperuserTest() {
        // Arrange
        var cookie = entity(CookieAccessToken.class);

        // Act
        this.componentUnderTest.deleteAllExceptCurrentAsSuperuser(cookie);

        // Assert
        verify(this.usersSessionsRepository).deleteExceptAccessToken(cookie);
    }
}
