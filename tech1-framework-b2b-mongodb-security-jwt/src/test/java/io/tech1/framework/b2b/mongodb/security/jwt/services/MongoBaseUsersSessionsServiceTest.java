package io.tech1.framework.b2b.mongodb.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.domain.events.EventSessionAddUserRequestMetadata;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersSessionsService;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityJwtTokenUtils;
import io.tech1.framework.b2b.base.security.jwt.utils.impl.SecurityJwtTokenUtilsImpl;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUserSession;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUsersSessionsRepository;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.constants.StringConstants.UNDEFINED;
import static io.tech1.framework.domain.utilities.random.EntityUtility.entity;
import static io.tech1.framework.domain.utilities.random.RandomUtility.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
@ContextConfiguration(loader= AnnotationConfigContextLoader.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class MongoBaseUsersSessionsServiceTest {

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
        MongoUsersSessionsRepository userSessionRepository() {
            return mock(MongoUsersSessionsRepository.class);
        }

        @Bean
        GeoLocationFacadeUtility geoLocationFacadeUtility() {
            return mock(GeoLocationFacadeUtility.class);
        }

        @Bean
        public SecurityJwtTokenUtils securityJwtTokenUtility() {
            return new SecurityJwtTokenUtilsImpl(
                    this.applicationFrameworkProperties
            );
        }

        @Bean
        UserAgentDetailsUtility userAgentDetailsUtility() {
            return new UserAgentDetailsUtilityImpl();
        }

        @Bean
        BaseUsersSessionsService userSessionService() {
            return new MongoBaseUsersSessionsService(
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
    private final MongoUsersSessionsRepository mongoUsersSessionsRepository;
    // Utilities
    private final GeoLocationFacadeUtility geoLocationFacadeUtility;
    private final UserAgentDetailsUtility userAgentDetailsUtility;

    private final BaseUsersSessionsService componentUnderTest;

    @BeforeEach
    void beforeEach() {
        reset(
                this.securityJwtPublisher,
                this.mongoUsersSessionsRepository,
                this.geoLocationFacadeUtility
        );
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(
                this.securityJwtPublisher,
                this.mongoUsersSessionsRepository,
                this.geoLocationFacadeUtility
        );
    }

    @Test
    void saveUserRequestMetadataTest() {
        // Arrange
        var event = entity(EventSessionAddUserRequestMetadata.class);
        var userSession = entity(MongoDbUserSession.class);
        var geoLocation = randomGeoLocation();
        when(this.geoLocationFacadeUtility.getGeoLocation(event.clientIpAddr())).thenReturn(geoLocation);
        when(this.mongoUsersSessionsRepository.getById(event.userSessionId())).thenReturn(userSession);
        var userSessionAC = ArgumentCaptor.forClass(MongoDbUserSession.class);

        // Act
        this.componentUnderTest.saveUserRequestMetadata(event);

        // Assert
        verify(this.geoLocationFacadeUtility).getGeoLocation(event.clientIpAddr());
        verify(this.mongoUsersSessionsRepository).getById(event.userSessionId());
        verify(this.mongoUsersSessionsRepository).save(userSessionAC.capture());
        var requestMetadata = userSessionAC.getValue().getRequestMetadata();
        assertThat(requestMetadata.getGeoLocation()).isEqualTo(geoLocation);
        assertThat(requestMetadata.getUserAgentDetails()).isEqualTo(this.userAgentDetailsUtility.getUserAgentDetails(event.userAgentHeader()));
    }

    @SuppressWarnings("unchecked")
    @Test
    void deleteAllExceptCurrentTest() {
        // Arrange
        var username = entity(Username.class);
        var cookie = entity(CookieRefreshToken.class);
        var session1 = new MongoDbUserSession(new JwtRefreshToken("session1"), randomUsername(), entity(UserRequestMetadata.class));
        var session2 = new MongoDbUserSession(new JwtRefreshToken("session2"), randomUsername(), entity(UserRequestMetadata.class));
        var session3 = new MongoDbUserSession(new JwtRefreshToken("session3"), randomUsername(), entity(UserRequestMetadata.class));
        var session4 = new MongoDbUserSession(new JwtRefreshToken("session4"), randomUsername(), entity(UserRequestMetadata.class));
        var sessions = new ArrayList<>(List.of(session1, session2, session3, session4));
        when(this.mongoUsersSessionsRepository.findByUsername(username)).thenReturn(sessions);
        when(this.mongoUsersSessionsRepository.findByRefreshToken(cookie.getJwtRefreshToken())).thenReturn(session3);

        // Act
        this.componentUnderTest.deleteAllExceptCurrent(username, cookie);

        // Assert
        verify(this.mongoUsersSessionsRepository).findByUsername(username);
        verify(this.mongoUsersSessionsRepository).findByRefreshToken(cookie.getJwtRefreshToken());
        var sessionsAC = ArgumentCaptor.forClass(List.class);
        verify(this.mongoUsersSessionsRepository).deleteAll(sessionsAC.capture());
        assertThat(sessionsAC.getValue()).hasSize(3);
        assertThat(((List<MongoDbUserSession>) sessionsAC.getValue()).stream().map(MongoDbUserSession::getId).collect(Collectors.toSet())).containsExactlyInAnyOrder(
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
        var session1 = new MongoDbUserSession(new JwtRefreshToken("session1"), randomUsername(), entity(UserRequestMetadata.class));
        var session2 = new MongoDbUserSession(new JwtRefreshToken("session2"), randomUsername(), entity(UserRequestMetadata.class));
        var session3 = new MongoDbUserSession(new JwtRefreshToken("session3"), randomUsername(), entity(UserRequestMetadata.class));
        var session4 = new MongoDbUserSession(new JwtRefreshToken("session4"), randomUsername(), entity(UserRequestMetadata.class));
        var sessions = new ArrayList<>(List.of(session1, session2, session3, session4));
        when(this.mongoUsersSessionsRepository.findAll()).thenReturn(sessions);
        when(this.mongoUsersSessionsRepository.findByRefreshToken(cookie.getJwtRefreshToken())).thenReturn(session3);

        // Act
        this.componentUnderTest.deleteAllExceptCurrentAsSuperuser(cookie);

        // Assert
        verify(this.mongoUsersSessionsRepository).findAll();
        verify(this.mongoUsersSessionsRepository).findByRefreshToken(cookie.getJwtRefreshToken());
        var sessionsAC = ArgumentCaptor.forClass(List.class);
        verify(this.mongoUsersSessionsRepository).deleteAll(sessionsAC.capture());
        assertThat(sessionsAC.getValue()).hasSize(3);
        assertThat(((List<MongoDbUserSession>) sessionsAC.getValue()).stream().map(MongoDbUserSession::getId).collect(Collectors.toSet())).containsExactlyInAnyOrder(
                "session1",
                "session2",
                "session4"
        );
    }
}
