package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbUserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.events.EventSessionAddUserRequestMetadata;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbUsersSessionsRepository;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityJwtTokenUtils;
import io.tech1.framework.b2b.base.security.jwt.utils.impl.SecurityJwtTokenUtilsImpl;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.constants.StringConstants;
import io.tech1.framework.domain.enums.Status;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.tests.constants.TestsConstants;
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

import static io.tech1.framework.domain.constants.StringConstants.UNDEFINED;
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
        AnyDbUsersSessionsRepository usersSessionsRepository() {
            return mock(AnyDbUsersSessionsRepository.class);
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
            ) {
                @Override
                public void deleteAllExceptCurrent(Username username, CookieRefreshToken cookie) {

                }

                @Override
                public void deleteAllExceptCurrentAsSuperuser(CookieRefreshToken cookie) {

                }
            };
        }
    }

    // Publishers
    protected final SecurityJwtPublisher securityJwtPublisher;
    // Repositories
    protected final AnyDbUsersSessionsRepository anyDbUsersSessionsRepository;
    // Utilities
    protected final GeoLocationFacadeUtility geoLocationFacadeUtility;
    protected final SecurityJwtTokenUtils securityJwtTokenUtils;
    protected final UserAgentDetailsUtility userAgentDetailsUtility;

    private final AbstractBaseUsersSessionsService componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.securityJwtPublisher,
                this.anyDbUsersSessionsRepository,
                this.geoLocationFacadeUtility
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.securityJwtPublisher,
                this.anyDbUsersSessionsRepository,
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
        var jwtUser = entity(JwtUser.class);
        var username = jwtUser.username();
        var jwtRefreshToken = entity(JwtRefreshToken.class);
        var userSession = new AnyDbUserSession(new UserSessionId(jwtRefreshToken.value()), username, entity(UserRequestMetadata.class), jwtRefreshToken);
        var savedUserSessionId = entity(UserSessionId.class);
        when(this.anyDbUsersSessionsRepository.findByRefreshTokenAsAny(jwtRefreshToken)).thenReturn(userSession);
        when(this.anyDbUsersSessionsRepository.saveAs(any())).thenReturn(savedUserSessionId);

        // Act
        this.componentUnderTest.save(jwtUser, jwtRefreshToken, httpServletRequest);

        // Assert
        verify(this.anyDbUsersSessionsRepository).findByRefreshTokenAsAny(jwtRefreshToken);
        var dbUserSessionAC = ArgumentCaptor.forClass(AnyDbUserSession.class);
        verify(this.anyDbUsersSessionsRepository).saveAs(dbUserSessionAC.capture());
        var actualDbUserSession = dbUserSessionAC.getValue();
        assertThat(actualDbUserSession.username()).isEqualTo(username);
        assertThat(actualDbUserSession.jwtRefreshToken()).isEqualTo(jwtRefreshToken);
        var requestMetadata = actualDbUserSession.metadata();
        assertThat(requestMetadata.getStatus()).isEqualTo(Status.STARTED);
        assertThat(requestMetadata.getGeoLocation().getIpAddr()).isEqualTo(ipAddr);
        var whereTuple3 = requestMetadata.getWhereTuple3();
        assertThat(whereTuple3.a()).isEqualTo(ipAddr);
        assertThat(whereTuple3.b()).isEqualTo(StringConstants.NO_FLAG);
        assertThat(whereTuple3.c()).isEqualTo("Processing...Please wait!");
        var whatTuple2 = requestMetadata.getWhatTuple2();
        assertThat(whatTuple2.a()).isEqualTo(UNDEFINED);
        assertThat(whatTuple2.b()).isEqualTo("—");
        assertThat(actualDbUserSession.id()).isNotNull();
        var eventAC = ArgumentCaptor.forClass(EventSessionAddUserRequestMetadata.class);
        verify(this.securityJwtPublisher).publishSessionAddUserRequestMetadata(eventAC.capture());
        var event = eventAC.getValue();
        assertThat(event.username()).isEqualTo(username);
        assertThat(event.userSessionId()).isEqualTo(savedUserSessionId);
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
        var savedUserSessionId = entity(UserSessionId.class);
        when(this.anyDbUsersSessionsRepository.saveAs(any())).thenReturn(savedUserSessionId);

        // Act
        this.componentUnderTest.save(jwtUser, jwtRefreshToken, httpServletRequest);

        // Assert
        verify(this.anyDbUsersSessionsRepository).findByRefreshTokenAsAny(jwtRefreshToken);
        var dbUserSessionAC = ArgumentCaptor.forClass(AnyDbUserSession.class);
        verify(this.anyDbUsersSessionsRepository).saveAs(dbUserSessionAC.capture());
        var actualDbUserSession = dbUserSessionAC.getValue();
        assertThat(actualDbUserSession.username()).isEqualTo(username);
        assertThat(actualDbUserSession.jwtRefreshToken()).isEqualTo(jwtRefreshToken);
        var requestMetadata = actualDbUserSession.metadata();
        assertThat(requestMetadata.getStatus()).isEqualTo(Status.STARTED);
        assertThat(requestMetadata.getGeoLocation().getIpAddr()).isEqualTo(ipAddr);
        var whereTuple3 = requestMetadata.getWhereTuple3();
        assertThat(whereTuple3.a()).isEqualTo(ipAddr);
        assertThat(whereTuple3.b()).isEqualTo(StringConstants.NO_FLAG);
        assertThat(whereTuple3.c()).isEqualTo("Processing...Please wait!");
        var whatTuple2 = requestMetadata.getWhatTuple2();
        assertThat(whatTuple2.a()).isEqualTo(UNDEFINED);
        assertThat(whatTuple2.b()).isEqualTo("—");
        assertThat(actualDbUserSession.id()).isNotNull();
        var eventAC = ArgumentCaptor.forClass(EventSessionAddUserRequestMetadata.class);
        verify(this.securityJwtPublisher).publishSessionAddUserRequestMetadata(eventAC.capture());
        var event = eventAC.getValue();
        assertThat(event.username()).isEqualTo(username);
        assertThat(event.userSessionId()).isEqualTo(savedUserSessionId);
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
        var oldUserSession = new AnyDbUserSession(new UserSessionId(oldJwtRefreshToken.value()), randomUsername(), entity(UserRequestMetadata.class), oldJwtRefreshToken);
        when(this.anyDbUsersSessionsRepository.findByRefreshTokenAsAny(oldJwtRefreshToken)).thenReturn(oldUserSession);

        // Act
        var jwtRefreshToken = this.componentUnderTest.refresh(jwtUser, oldJwtRefreshToken, newJwtRefreshToken, httpServletRequest);

        // Assert
        verify(this.anyDbUsersSessionsRepository).findByRefreshTokenAsAny(oldJwtRefreshToken);
        var saveCaptor = ArgumentCaptor.forClass(AnyDbUserSession.class);
        verify(this.anyDbUsersSessionsRepository).saveAs(saveCaptor.capture());
        var newUserSession = saveCaptor.getValue();
        assertThat(newUserSession.username()).isEqualTo(username);
        assertThat(newUserSession.jwtRefreshToken()).isEqualTo(newJwtRefreshToken);
        assertThat(newUserSession.metadata()).isEqualTo(oldUserSession.metadata());
        verify(this.anyDbUsersSessionsRepository).delete(oldUserSession.id());
        var eventAC = ArgumentCaptor.forClass(EventSessionAddUserRequestMetadata.class);
        verify(this.securityJwtPublisher).publishSessionAddUserRequestMetadata(eventAC.capture());
        var event = eventAC.getValue();
        assertThat(event.username()).isEqualTo(username);
        assertThat(event.userSessionId()).isEqualTo(newUserSession.id());
        assertThat(event.isAuthenticationLoginEndpoint()).isFalse();
        assertThat(event.isAuthenticationRefreshTokenEndpoint()).isTrue();
        assertThat(jwtRefreshToken).isEqualTo(newUserSession.jwtRefreshToken());
    }

    @Test
    void saveUserRequestMetadataTest() {
        // Arrange
        var event = entity(EventSessionAddUserRequestMetadata.class);
        var userSession = entity(AnyDbUserSession.class);
        var geoLocation = randomGeoLocation();
        when(this.geoLocationFacadeUtility.getGeoLocation(event.clientIpAddr())).thenReturn(geoLocation);
        when(this.anyDbUsersSessionsRepository.getById(event.userSessionId())).thenReturn(userSession);
        var userSessionAC = ArgumentCaptor.forClass(AnyDbUserSession.class);

        // Act
        this.componentUnderTest.saveUserRequestMetadata(event);

        // Assert
        verify(this.geoLocationFacadeUtility).getGeoLocation(event.clientIpAddr());
        verify(this.anyDbUsersSessionsRepository).getById(event.userSessionId());
        verify(this.anyDbUsersSessionsRepository).saveAs(userSessionAC.capture());
        var requestMetadata = userSessionAC.getValue().metadata();
        assertThat(requestMetadata.getGeoLocation()).isEqualTo(geoLocation);
        assertThat(requestMetadata.getUserAgentDetails()).isEqualTo(this.userAgentDetailsUtility.getUserAgentDetails(event.userAgentHeader()));
    }

    @Test
    void getExpiredSessionsTest() {
        // Arrange
        var usernames = new HashSet<>(Set.of(TestsConstants.TECH1));
        var sessionInvalidUserSession = new AnyDbUserSession(
                entity(UserSessionId.class),
                randomUsername(),
                entity(UserRequestMetadata.class),
                new JwtRefreshToken("<invalid>")
        );
        var sessionExpiredUserSession = new AnyDbUserSession(
                entity(UserSessionId.class),
                randomUsername(),
                entity(UserRequestMetadata.class),
                new JwtRefreshToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdWx0aXVzZXI0MyIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJhZG1pbiJ9LHsiYXV0aG9yaXR5IjoidXNlciJ9XSwiaWF0IjoxNjQyNzc0NTk3LCJleHAiOjE2NDI3NzQ2Mjd9.aCeKIy8uvei_c_aXoHlVhQ1N8wmjfguXgi2fWMRYVp8")
        );
        var sessionAliveUserSession = new AnyDbUserSession(
                entity(UserSessionId.class),
                randomUsername(),
                entity(UserRequestMetadata.class),
                new JwtRefreshToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtdWx0aXVzZXI0MyIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJhZG1pbiJ9LHsiYXV0aG9yaXR5IjoidXNlciJ9XSwiaWF0IjoxNjQyNzc0Nzc4LCJleHAiOjQ3OTg0NDgzNzh9._BMUZR3wls5O1BYDm_4loYi3vn70GjE39Cpuqh-Z_bY")
        );
        var usersSessions = List.of(sessionInvalidUserSession, sessionExpiredUserSession, sessionAliveUserSession);
        when(this.anyDbUsersSessionsRepository.findByUsernameInAsAny(usernames)).thenReturn(usersSessions);

        // Act
        var sessionsValidatedTuple2 = this.componentUnderTest.getExpiredSessions(usernames);

        // Assert
        verify(this.anyDbUsersSessionsRepository).findByUsernameInAsAny(usernames);
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
    void deleteByIdTest() {
        // Arrange
        var sessionId = entity(UserSessionId.class);

        // Act
        this.componentUnderTest.deleteById(sessionId);

        // Assert
        verify(this.anyDbUsersSessionsRepository).delete(sessionId);
    }
}
