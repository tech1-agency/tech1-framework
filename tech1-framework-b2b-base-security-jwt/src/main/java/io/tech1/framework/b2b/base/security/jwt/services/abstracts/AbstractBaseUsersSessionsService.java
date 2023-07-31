package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbUserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.events.EventSessionAddUserRequestMetadata;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
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
import io.tech1.framework.domain.tuples.Tuple3;
import io.tech1.framework.utilities.browsers.UserAgentDetailsUtility;
import io.tech1.framework.utilities.geo.facades.GeoLocationFacadeUtility;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static io.tech1.framework.domain.utilities.http.HttpServletRequestUtility.getClientIpAddr;
import static io.tech1.framework.domain.utilities.time.TimestampUtility.isPast;
import static java.util.Objects.isNull;

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
    public void save(JwtUser user, JwtRefreshToken jwtRefreshToken, HttpServletRequest httpServletRequest) {
        var username = user.username();
        var userSession = this.anyDbUsersSessionsRepository.findByRefreshTokenAsAny(jwtRefreshToken);
        var clientIpAddr = getClientIpAddr(httpServletRequest);
        var userRequestMetadata = UserRequestMetadata.processing(clientIpAddr);
        if (isNull(userSession)) {
            userSession = new AnyDbUserSession(
                    new UserSessionId(jwtRefreshToken.value()),
                    username,
                    userRequestMetadata,
                    jwtRefreshToken
            );
        } else {
            userSession = new AnyDbUserSession(
                    userSession.id(),
                    userSession.username(),
                    userRequestMetadata,
                    userSession.jwtRefreshToken()
            );
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
    public JwtRefreshToken refresh(JwtUser user, JwtRefreshToken oldJwtRefreshToken, JwtRefreshToken newJwtRefreshToken, HttpServletRequest httpServletRequest) {
        var username = user.username();
        var oldUserSession = this.anyDbUsersSessionsRepository.findByRefreshTokenAsAny(oldJwtRefreshToken);
        var newUserSession = new AnyDbUserSession(
                UserSessionId.of(newJwtRefreshToken.value()),
                username,
                oldUserSession.metadata(),
                newJwtRefreshToken
        );
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
        return newUserSession.jwtRefreshToken();
    }

    @Override
    public SessionsExpiredTable getExpiredSessions(Set<Username> usernames) {
        var usersSessions = this.anyDbUsersSessionsRepository.findByUsernameInAsAny(usernames);
        List<Tuple3<Username, UserRequestMetadata, JwtRefreshToken>> expiredSessions = new ArrayList<>();
        List<UserSessionId> expiredOrInvalidSessionIds = new ArrayList<>();

        usersSessions.forEach(userSession -> {
            var sessionId = userSession.id();
            var validatedClaims = this.securityJwtTokenUtils.validate(userSession.jwtRefreshToken());
            var isValid = validatedClaims.valid();
            if (isValid) {
                var isExpired = isPast(validatedClaims.safeGetExpirationTimestamp());
                if (isExpired) {
                    expiredOrInvalidSessionIds.add(sessionId);
                    expiredSessions.add(
                            new Tuple3<>(
                                    validatedClaims.safeGetUsername(),
                                    userSession.metadata(),
                                    userSession.jwtRefreshToken()
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
}
