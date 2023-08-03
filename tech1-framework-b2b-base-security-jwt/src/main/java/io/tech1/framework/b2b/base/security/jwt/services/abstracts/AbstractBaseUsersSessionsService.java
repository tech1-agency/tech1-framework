package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.events.EventSessionAddUserRequestMetadata;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.domain.sessions.SessionsExpiredTable;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbUsersSessionsRepository;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersSessionsService;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityJwtTokenUtils;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserAgentHeader;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.tuples.Tuple2;
import io.tech1.framework.domain.tuples.Tuple3;
import io.tech1.framework.utilities.browsers.UserAgentDetailsUtility;
import io.tech1.framework.utilities.geo.facades.GeoLocationFacadeUtility;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbUserSession.ofNotPersisted;
import static io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbUserSession.ofPersisted;
import static io.tech1.framework.domain.utilities.http.HttpServletRequestUtility.getClientIpAddr;
import static io.tech1.framework.domain.utilities.time.TimestampUtility.isPast;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractBaseUsersSessionsService implements BaseUsersSessionsService {

    // Publishers
    protected final SecurityJwtPublisher securityJwtPublisher;
    // Repositories
    protected final AnyDbUsersSessionsRepository anyDbUsersSessionsRepository;
    // Utilities
    protected final GeoLocationFacadeUtility geoLocationFacadeUtility;
    protected final SecurityJwtTokenUtils securityJwtTokenUtils;
    protected final UserAgentDetailsUtility userAgentDetailsUtility;

    @Override
    public void save(JwtUser user, JwtAccessToken accessToken, JwtRefreshToken refreshToken, HttpServletRequest httpServletRequest) {
        var username = user.username();
        var userSessionTP = this.anyDbUsersSessionsRepository.isPresent(accessToken);
        var clientIpAddr = getClientIpAddr(httpServletRequest);
        var metadata = UserRequestMetadata.processing(clientIpAddr);
        var userSession = userSessionTP.value();
        if (userSessionTP.present()) {
            userSession = ofPersisted(userSession.id(), userSession.username(), userSession.accessToken(), userSession.refreshToken(), metadata);
        } else {
            userSession = ofNotPersisted(username, accessToken, refreshToken, metadata);
        }
        var sessionId = this.anyDbUsersSessionsRepository.saveAs(userSession);
        this.securityJwtPublisher.publishSessionAddUserRequestMetadata(
                new EventSessionAddUserRequestMetadata(
                        username,
                        user.email(),
                        sessionId,
                        clientIpAddr,
                        new UserAgentHeader(httpServletRequest),
                        true,
                        false
                )
        );
    }

    @Override
    public void refresh(JwtUser user, JwtAccessToken accessToken, JwtRefreshToken oldRefreshToken, JwtRefreshToken newRefreshToken, HttpServletRequest httpServletRequest) {
        var username = user.username();
        // TODO [YY] change oldRefreshToken:  JwtRefreshToken -> AnyDbUserSession
        // WARNING: old refresh token already validated and present in database
        var oldUserSession = this.anyDbUsersSessionsRepository.isPresent(oldRefreshToken).value();
        var newUserSession = ofNotPersisted(username, accessToken, newRefreshToken, oldUserSession.metadata());
        this.anyDbUsersSessionsRepository.saveAs(newUserSession);
        this.anyDbUsersSessionsRepository.delete(oldUserSession.id());
        this.securityJwtPublisher.publishSessionAddUserRequestMetadata(
                new EventSessionAddUserRequestMetadata(
                        username,
                        user.email(),
                        newUserSession.id(),
                        getClientIpAddr(httpServletRequest),
                        new UserAgentHeader(httpServletRequest),
                        false,
                        true
                )
        );
    }

    @Override
    public Tuple2<UserSessionId, UserRequestMetadata> saveUserRequestMetadata(EventSessionAddUserRequestMetadata event) {
        var geoLocation = this.geoLocationFacadeUtility.getGeoLocation(event.clientIpAddr());
        var userAgentDetails = this.userAgentDetailsUtility.getUserAgentDetails(event.userAgentHeader());
        // TODO [YY] consider event.userSessionId() -> event.anyDb
        // WARNING: already validated and present in database
        var userSession = this.anyDbUsersSessionsRepository.isPresent(event.userSessionId()).value();
        userSession = ofPersisted(
                userSession.id(),
                userSession.username(),
                userSession.accessToken(),
                userSession.refreshToken(),
                UserRequestMetadata.processed(geoLocation, userAgentDetails)
        );
        this.anyDbUsersSessionsRepository.saveAs(userSession);
        return new Tuple2<>(userSession.id(), userSession.metadata());
    }

    @Override
    public SessionsExpiredTable getExpiredRefreshTokensSessions(Set<Username> usernames) {
        var usersSessions = this.anyDbUsersSessionsRepository.findByUsernameInAsAny(usernames);
        List<Tuple3<Username, JwtRefreshToken, UserRequestMetadata>> expiredSessions = new ArrayList<>();
        List<UserSessionId> expiredOrInvalidSessionIds = new ArrayList<>();

        usersSessions.forEach(userSession -> {
            var sessionId = userSession.id();
            var validatedClaims = this.securityJwtTokenUtils.validate(userSession.refreshToken());
            var isValid = validatedClaims.valid();
            if (isValid) {
                var isExpired = isPast(validatedClaims.getExpirationTimestamp());
                if (isExpired) {
                    expiredOrInvalidSessionIds.add(sessionId);
                    expiredSessions.add(
                            new Tuple3<>(
                                    validatedClaims.username(),
                                    userSession.refreshToken(),
                                    userSession.metadata()
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
    public void deleteById(UserSessionId sessionId) {
        this.anyDbUsersSessionsRepository.delete(sessionId);
    }

    @Override
    public void deleteAllExceptCurrent(Username username, CookieAccessToken cookie) {
        this.anyDbUsersSessionsRepository.deleteByUsernameExceptAccessToken(username, cookie);
    }

    @Override
    public void deleteAllExceptCurrentAsSuperuser(CookieAccessToken cookie) {
        this.anyDbUsersSessionsRepository.deleteExceptAccessToken(cookie);
    }
}
