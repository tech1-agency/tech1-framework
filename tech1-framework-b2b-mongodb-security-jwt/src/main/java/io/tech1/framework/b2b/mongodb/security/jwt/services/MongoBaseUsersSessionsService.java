package io.tech1.framework.b2b.mongodb.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.domain.events.EventSessionAddUserRequestMetadata;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.base.security.jwt.services.abstracts.AbstractBaseUsersSessionsService;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityJwtTokenUtils;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUsersSessionsRepository;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.tuples.Tuple2;
import io.tech1.framework.utilities.browsers.UserAgentDetailsUtility;
import io.tech1.framework.utilities.geo.facades.GeoLocationFacadeUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Service
public class MongoBaseUsersSessionsService extends AbstractBaseUsersSessionsService {

    // Repositories
    private final MongoUsersSessionsRepository usersSessionsRepository;
    // Utilities
    private final GeoLocationFacadeUtility geoLocationFacadeUtility;
    private final UserAgentDetailsUtility userAgentDetailsUtility;

    @Autowired
    public MongoBaseUsersSessionsService(
            SecurityJwtPublisher securityJwtPublisher,
            MongoUsersSessionsRepository usersSessionsRepository,
            GeoLocationFacadeUtility geoLocationFacadeUtility,
            SecurityJwtTokenUtils securityJwtTokenUtils,
            UserAgentDetailsUtility userAgentDetailsUtility
    ) {
        super(
                securityJwtPublisher,
                usersSessionsRepository,
                geoLocationFacadeUtility,
                securityJwtTokenUtils,
                userAgentDetailsUtility
        );
        this.usersSessionsRepository = usersSessionsRepository;
        this.geoLocationFacadeUtility = geoLocationFacadeUtility;
        this.userAgentDetailsUtility = userAgentDetailsUtility;
    }

    @Override
    public Tuple2<UserSessionId, UserRequestMetadata> saveUserRequestMetadata(EventSessionAddUserRequestMetadata event) {
        var geoLocation = this.geoLocationFacadeUtility.getGeoLocation(event.clientIpAddr());
        var userAgentDetails = this.userAgentDetailsUtility.getUserAgentDetails(event.userAgentHeader());
        var requestMetadata = UserRequestMetadata.processed(geoLocation, userAgentDetails);
        var userSession = this.usersSessionsRepository.getById(event.userSessionId());
        userSession.setRequestMetadata(requestMetadata);
        this.usersSessionsRepository.save(userSession);
        return new Tuple2<>(userSession.userSessionId(), userSession.getRequestMetadata());
    }

    @Override
    public void deleteAllExceptCurrent(Username username, CookieRefreshToken cookie) {
        var sessions = this.usersSessionsRepository.findByUsername(username);
        var currentSession = this.usersSessionsRepository.findByRefreshToken(cookie.getJwtRefreshToken());
        sessions.removeIf(session -> session.getId().equals(currentSession.getId()));
        this.usersSessionsRepository.deleteAll(sessions);
    }

    @Override
    public void deleteAllExceptCurrentAsSuperuser(CookieRefreshToken cookie) {
        var sessions = this.usersSessionsRepository.findAll();
        var currentSession = this.usersSessionsRepository.findByRefreshToken(cookie.getJwtRefreshToken());
        sessions.removeIf(session -> session.getId().equals(currentSession.getId()));
        this.usersSessionsRepository.deleteAll(sessions);
    }
}
