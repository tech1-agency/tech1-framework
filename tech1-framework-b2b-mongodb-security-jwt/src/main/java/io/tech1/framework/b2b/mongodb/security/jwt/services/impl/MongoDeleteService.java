package io.tech1.framework.b2b.mongodb.security.jwt.services.impl;

import io.tech1.framework.b2b.base.security.jwt.domain.events.EventSessionAddUserRequestMetadata;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.base.security.jwt.services.DeleteService;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUserSession;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUserSessionsRepository;
import io.tech1.framework.domain.http.requests.UserAgentHeader;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.tuples.Tuple2;
import io.tech1.framework.utilities.browsers.UserAgentDetailsUtility;
import io.tech1.framework.utilities.geo.facades.GeoLocationFacadeUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static io.tech1.framework.domain.utilities.http.HttpServletRequestUtility.getClientIpAddr;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MongoDeleteService implements DeleteService {

    // Publishers
    private final SecurityJwtPublisher securityJwtPublisher;
    // Repositories
    private final MongoUserSessionsRepository mongoUserSessionsRepository;
    // Utilities
    private final GeoLocationFacadeUtility geoLocationFacadeUtility;
    private final UserAgentDetailsUtility userAgentDetailsUtility;

    // TODO [YY] reuse UserSessionService
    @Deprecated
    @Override
    public Tuple2<UserSessionId, UserRequestMetadata> saveUserRequestMetadata(EventSessionAddUserRequestMetadata event) {
        var geoLocation = this.geoLocationFacadeUtility.getGeoLocation(event.clientIpAddr());
        var userAgentDetails = this.userAgentDetailsUtility.getUserAgentDetails(event.userAgentHeader());
        var requestMetadata = UserRequestMetadata.processed(geoLocation, userAgentDetails);
        var userSession = this.mongoUserSessionsRepository.getById(event.userSessionId().value());
        userSession.setRequestMetadata(requestMetadata);
        this.mongoUserSessionsRepository.save(userSession);
        return new Tuple2<>(userSession.userSessionId(), userSession.getRequestMetadata());
    }

    // TODO [YY] reuse UserSessionService
    @Deprecated
    @Override
    public JwtRefreshToken refresh(JwtUser user, JwtRefreshToken oldJwtRefreshToken, JwtRefreshToken newJwtRefreshToken, HttpServletRequest httpServletRequest) {
        var username = user.username();
        var oldUserSession = this.mongoUserSessionsRepository.findByRefreshToken(oldJwtRefreshToken);
        var newUserSession = new MongoDbUserSession(
                newJwtRefreshToken,
                username,
                oldUserSession.getRequestMetadata()
        );
        this.mongoUserSessionsRepository.save(newUserSession);
        this.mongoUserSessionsRepository.delete(oldUserSession);
        this.securityJwtPublisher.publishSessionAddUserRequestMetadata(
                new EventSessionAddUserRequestMetadata(
                        username,
                        user.email(),
                        newUserSession.userSessionId(),
                        getClientIpAddr(httpServletRequest),
                        new UserAgentHeader(httpServletRequest),
                        false,
                        true
                )
        );
        return newUserSession.getJwtRefreshToken();
    }
}
