package io.tech1.framework.b2b.mongodb.security.jwt.services.impl;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUserSession;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.events.EventSessionAddUserRequestMetadata;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.session.SessionsValidatedTuple2;
import io.tech1.framework.b2b.mongodb.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.mongodb.security.jwt.repositories.UserSessionRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.services.UserSessionService;
import io.tech1.framework.b2b.mongodb.security.jwt.utilities.SecurityJwtTokenUtility;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.tuples.Tuple2;
import io.tech1.framework.domain.utilities.http.HttpServletRequestUtility;
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

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserSessionServiceImpl implements UserSessionService {

    // Publishers
    private final SecurityJwtPublisher securityJwtPublisher;
    // Repositories
    private final UserSessionRepository userSessionRepository;
    // Utilities
    private final GeoLocationFacadeUtility geoLocationFacadeUtility;
    private final SecurityJwtTokenUtility securityJwtTokenUtility;

    @Override
    public List<DbUserSession> findByUsername(Username username) {
        return this.userSessionRepository.findByUsername(username);
    }

    @Override
    public List<DbUserSession> findByUsernameIn(Set<Username> usernames) {
        return this.userSessionRepository.findByUsernameIn(usernames);
    }

    @Override
    public Long deleteByIdIn(List<String> ids) {
        return this.userSessionRepository.deleteByIdIn(ids);
    }

    @Override
    public DbUserSession findByRefreshToken(JwtRefreshToken jwtRefreshToken) {
        return this.userSessionRepository.findByRefreshToken(jwtRefreshToken);
    }

    @Override
    public void deleteByRefreshToken(JwtRefreshToken jwtRefreshToken) {
        this.userSessionRepository.deleteByRefreshToken(jwtRefreshToken);
    }

    @Override
    public DbUserSession save(DbUser user, JwtRefreshToken jwtRefreshToken, HttpServletRequest httpServletRequest) {
        var username = user.getUsername();
        var userSession = this.userSessionRepository.findByRefreshToken(jwtRefreshToken);
        var clientIpAddr = getClientIpAddr(httpServletRequest);
        var userRequestMetadata = UserRequestMetadata.processing(clientIpAddr);
        if (isNull(userSession)) {
            userSession = new DbUserSession(
                    jwtRefreshToken,
                    username,
                    userRequestMetadata
            );
        } else {
            userSession.editRequestMetadata(userRequestMetadata);
        }
        userSession = this.userSessionRepository.save(userSession);
        this.securityJwtPublisher.publishSessionAddUserRequestMetadata(
                EventSessionAddUserRequestMetadata.of(
                        username,
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
    public DbUserSession refresh(DbUser user, JwtRefreshToken oldJwtRefreshToken, JwtRefreshToken newJwtRefreshToken, HttpServletRequest httpServletRequest) {
        var username = user.getUsername();
        var oldUserSession = this.userSessionRepository.findByRefreshToken(oldJwtRefreshToken);
        var newUserSession = new DbUserSession(
                newJwtRefreshToken,
                username,
                oldUserSession.getRequestMetadata()
        );
        this.userSessionRepository.save(newUserSession);
        this.userSessionRepository.delete(oldUserSession);
        this.securityJwtPublisher.publishSessionAddUserRequestMetadata(
                EventSessionAddUserRequestMetadata.of(
                        username,
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
    public DbUserSession saveUserRequestMetadata(EventSessionAddUserRequestMetadata event) {
        var geoLocation = this.geoLocationFacadeUtility.getGeoLocation(event.getClientIpAddr());
        var userAgentDetails = HttpServletRequestUtility.getUserAgentDetails(event.getUserAgentHeader());
        var requestMetadata = UserRequestMetadata.processed(geoLocation, userAgentDetails);
        var userSession = event.getUserSession();
        userSession.editRequestMetadata(requestMetadata);
        return this.userSessionRepository.save(userSession);
    }

    @Override
    public SessionsValidatedTuple2 validate(List<DbUserSession> usersSessions) {
        List<Tuple2<Username, DbUserSession>> expiredSessions = new ArrayList<>();
        List<String> expiredOrInvalidSessionIds = new ArrayList<>();

        usersSessions.forEach(userSession -> {
            var sessionId = userSession.getId();
            var validatedClaims = this.securityJwtTokenUtility.validate(userSession.getJwtRefreshToken());
            var isValid = validatedClaims.isValid();
            if (isValid) {
                var isExpired = isPast(validatedClaims.safeGetExpirationTimestamp());
                if (isExpired) {
                    expiredOrInvalidSessionIds.add(sessionId);
                    expiredSessions.add(Tuple2.of(validatedClaims.safeGetUsername(), userSession));
                }
            } else {
                expiredOrInvalidSessionIds.add(sessionId);
            }
        });

        return SessionsValidatedTuple2.of(
                expiredSessions,
                expiredOrInvalidSessionIds
        );
    }
}
