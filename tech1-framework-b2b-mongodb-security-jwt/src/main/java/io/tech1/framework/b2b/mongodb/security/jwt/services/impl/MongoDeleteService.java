package io.tech1.framework.b2b.mongodb.security.jwt.services.impl;

import io.tech1.framework.b2b.base.security.jwt.domain.events.EventSessionAddUserRequestMetadata;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.services.DeleteService;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUserSessionRepository;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.tuples.Tuple2;
import io.tech1.framework.utilities.browsers.UserAgentDetailsUtility;
import io.tech1.framework.utilities.geo.facades.GeoLocationFacadeUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MongoDeleteService implements DeleteService {

    // Repositories
    private final MongoUserSessionRepository mongoUserSessionRepository;
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
        var userSession = this.mongoUserSessionRepository.getById(event.userSessionId().value());
        userSession.setRequestMetadata(requestMetadata);
        this.mongoUserSessionRepository.save(userSession);
        return new Tuple2<>(userSession.userSessionId(), userSession.getRequestMetadata());
    }
}
