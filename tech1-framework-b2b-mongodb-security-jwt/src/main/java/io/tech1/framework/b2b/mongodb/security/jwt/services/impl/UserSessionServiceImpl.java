package io.tech1.framework.b2b.mongodb.security.jwt.services.impl;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.sessions.SessionsValidatedTuple2;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityJwtTokenUtils;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUserSession;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.EventSessionAddUserRequestMetadata;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.mongodb.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.MongoUserSessionRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.services.UserSessionService;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.tuples.Tuple3;
import io.tech1.framework.utilities.browsers.UserAgentDetailsUtility;
import io.tech1.framework.utilities.geo.facades.GeoLocationFacadeUtility;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserSessionServiceImpl implements UserSessionService {

    // Publishers
    private final SecurityJwtPublisher securityJwtPublisher;
    // Repositories
    private final MongoUserSessionRepository mongoUserSessionRepository;
    // Utilities
    private final GeoLocationFacadeUtility geoLocationFacadeUtility;
    private final SecurityJwtTokenUtils securityJwtTokenUtils;
    private final UserAgentDetailsUtility userAgentDetailsUtility;

    @Override
    public List<MongoDbUserSession> findByUsername(Username username) {
        return this.mongoUserSessionRepository.findByUsername(username);
    }

    @Override
    public List<MongoDbUserSession> findByUsernameIn(Set<Username> usernames) {
        return this.mongoUserSessionRepository.findByUsernameIn(usernames);
    }

    @Override
    public Long deleteByIdIn(List<String> ids) {
        return this.mongoUserSessionRepository.deleteByIdIn(ids);
    }

    @Override
    public MongoDbUserSession findByRefreshToken(JwtRefreshToken jwtRefreshToken) {
        return this.mongoUserSessionRepository.findByRefreshToken(jwtRefreshToken);
    }

    @Override
    public void deleteByRefreshToken(JwtRefreshToken jwtRefreshToken) {
        this.mongoUserSessionRepository.deleteByRefreshToken(jwtRefreshToken);
    }

    @Override
    public MongoDbUserSession save(JwtUser user, JwtRefreshToken jwtRefreshToken, HttpServletRequest httpServletRequest) {
        var username = user.username();
        var userSession = this.mongoUserSessionRepository.findByRefreshToken(jwtRefreshToken);
        var clientIpAddr = getClientIpAddr(httpServletRequest);
        var userRequestMetadata = UserRequestMetadata.processing(clientIpAddr);
        if (isNull(userSession)) {
            userSession = new MongoDbUserSession(
                    jwtRefreshToken,
                    username,
                    userRequestMetadata
            );
        } else {
            userSession.editRequestMetadata(userRequestMetadata);
        }
        userSession = this.mongoUserSessionRepository.save(userSession);
        this.securityJwtPublisher.publishSessionAddUserRequestMetadata(
                EventSessionAddUserRequestMetadata.of(
                        username,
                        user.email(),
                        userSession,
                        clientIpAddr,
                        httpServletRequest,
                        true,
                        false
                )
        );
        return userSession;
    }

    @Override
    public MongoDbUserSession refresh(JwtUser user, JwtRefreshToken oldJwtRefreshToken, JwtRefreshToken newJwtRefreshToken, HttpServletRequest httpServletRequest) {
        var username = user.username();
        var oldUserSession = this.mongoUserSessionRepository.findByRefreshToken(oldJwtRefreshToken);
        var newUserSession = new MongoDbUserSession(
                newJwtRefreshToken,
                username,
                oldUserSession.getRequestMetadata()
        );
        this.mongoUserSessionRepository.save(newUserSession);
        this.mongoUserSessionRepository.delete(oldUserSession);
        this.securityJwtPublisher.publishSessionAddUserRequestMetadata(
                EventSessionAddUserRequestMetadata.of(
                        username,
                        user.email(),
                        newUserSession,
                        getClientIpAddr(httpServletRequest),
                        httpServletRequest,
                        false,
                        true
                )
        );
        return newUserSession;
    }

    @Override
    public MongoDbUserSession saveUserRequestMetadata(EventSessionAddUserRequestMetadata event) {
        var geoLocation = this.geoLocationFacadeUtility.getGeoLocation(event.clientIpAddr());
        var userAgentDetails = this.userAgentDetailsUtility.getUserAgentDetails(event.userAgentHeader());
        var requestMetadata = UserRequestMetadata.processed(geoLocation, userAgentDetails);
        var userSession = event.userSession();
        userSession.editRequestMetadata(requestMetadata);
        return this.mongoUserSessionRepository.save(userSession);
    }

    @Override
    public SessionsValidatedTuple2 validate(List<MongoDbUserSession> usersSessions) {
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

        return new SessionsValidatedTuple2(
                expiredSessions,
                expiredOrInvalidSessionIds
        );
    }

    @Override
    public void deleteById(String sessionId) {
        this.mongoUserSessionRepository.deleteById(sessionId);
    }

    @Override
    public void deleteAllExceptCurrent(Username username, CookieRefreshToken cookie) {
        var sessions = this.mongoUserSessionRepository.findByUsername(username);
        var currentSession = this.mongoUserSessionRepository.findByRefreshToken(cookie.getJwtRefreshToken());
        sessions.removeIf(session -> session.getId().equals(currentSession.getId()));
        this.mongoUserSessionRepository.deleteAll(sessions);
    }

    @Override
    public void deleteAllExceptCurrentAsSuperuser(CookieRefreshToken cookie) {
        var sessions = this.mongoUserSessionRepository.findAll();
        var currentSession = this.mongoUserSessionRepository.findByRefreshToken(cookie.getJwtRefreshToken());
        sessions.removeIf(session -> session.getId().equals(currentSession.getId()));
        this.mongoUserSessionRepository.deleteAll(sessions);
    }
}
