package io.tech1.framework.b2b.mongodb.security.jwt.services;

import io.tech1.framework.b2b.base.security.jwt.domain.events.EventSessionAddUserRequestMetadata;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.domain.sessions.SessionsExpiredTable;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.base.security.jwt.services.abstracts.AbstractBaseUsersSessionsService;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityJwtTokenUtils;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUserSession;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUsersSessionsRepository;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserAgentHeader;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.tuples.Tuple2;
import io.tech1.framework.domain.tuples.Tuple3;
import io.tech1.framework.utilities.browsers.UserAgentDetailsUtility;
import io.tech1.framework.utilities.geo.facades.GeoLocationFacadeUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static io.tech1.framework.domain.utilities.http.HttpServletRequestUtility.getClientIpAddr;
import static io.tech1.framework.domain.utilities.time.TimestampUtility.isPast;
import static java.util.Objects.isNull;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Slf4j
@Service
public class MongoBaseUsersSessionsService extends AbstractBaseUsersSessionsService {

    // Publishers
    private final SecurityJwtPublisher securityJwtPublisher;
    // Repositories
    private final MongoUsersSessionsRepository usersSessionsRepository;
    // Utilities
    private final GeoLocationFacadeUtility geoLocationFacadeUtility;
    private final SecurityJwtTokenUtils securityJwtTokenUtils;
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
        this.securityJwtPublisher = securityJwtPublisher;
        this.usersSessionsRepository = usersSessionsRepository;
        this.geoLocationFacadeUtility = geoLocationFacadeUtility;
        this.securityJwtTokenUtils = securityJwtTokenUtils;
        this.userAgentDetailsUtility = userAgentDetailsUtility;
    }

    @Override
    public void save(JwtUser user, JwtRefreshToken jwtRefreshToken, HttpServletRequest httpServletRequest) {
        var username = user.username();
        var userSession = this.usersSessionsRepository.findByRefreshToken(jwtRefreshToken);
        var clientIpAddr = getClientIpAddr(httpServletRequest);
        var userRequestMetadata = UserRequestMetadata.processing(clientIpAddr);
        if (isNull(userSession)) {
            userSession = new MongoDbUserSession(
                    jwtRefreshToken,
                    username,
                    userRequestMetadata
            );
        } else {
            userSession.setRequestMetadata(userRequestMetadata);
        }
        userSession = this.usersSessionsRepository.save(userSession);
        this.securityJwtPublisher.publishSessionAddUserRequestMetadata(
                new EventSessionAddUserRequestMetadata(
                        username,
                        user.email(),
                        userSession.userSessionId(),
                        clientIpAddr,
                        new UserAgentHeader(httpServletRequest),
                        true,
                        false
                )
        );
    }

    @Override
    public JwtRefreshToken refresh(JwtUser user, JwtRefreshToken oldJwtRefreshToken, JwtRefreshToken newJwtRefreshToken, HttpServletRequest httpServletRequest) {
        var username = user.username();
        var oldUserSession = this.usersSessionsRepository.findByRefreshToken(oldJwtRefreshToken);
        var newUserSession = new MongoDbUserSession(
                newJwtRefreshToken,
                username,
                oldUserSession.getRequestMetadata()
        );
        this.usersSessionsRepository.save(newUserSession);
        this.usersSessionsRepository.delete(oldUserSession);
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
    public SessionsExpiredTable getExpiredSessions(Set<Username> usernames) {
        var usersSessions = this.usersSessionsRepository.findByUsernameIn(usernames);
        List<Tuple3<Username, UserRequestMetadata, JwtRefreshToken>> expiredSessions = new ArrayList<>();
        List<String> expiredOrInvalidSessionIds = new ArrayList<>();

        usersSessions.forEach(userSession -> {
            var sessionId = userSession.getId();
            var validatedClaims = this.securityJwtTokenUtils.validate(userSession.getJwtRefreshToken());
            var isValid = validatedClaims.valid();
            if (isValid) {
                var isExpired = isPast(validatedClaims.safeGetExpirationTimestamp());
                if (isExpired) {
                    expiredOrInvalidSessionIds.add(sessionId);
                    expiredSessions.add(
                            new Tuple3<>(
                                    validatedClaims.safeGetUsername(),
                                    userSession.getRequestMetadata(),
                                    userSession.getJwtRefreshToken()
                            )
                    );
                }
            } else {
                expiredOrInvalidSessionIds.add(sessionId);
            }
        });

        return new SessionsExpiredTable(
                expiredSessions,
                expiredOrInvalidSessionIds
        );
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
