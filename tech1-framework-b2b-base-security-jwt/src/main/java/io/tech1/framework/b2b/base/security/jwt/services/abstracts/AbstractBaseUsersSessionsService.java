package io.tech1.framework.b2b.base.security.jwt.services.abstracts;

import io.tech1.framework.b2b.base.security.jwt.domain.db.UserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.events.EventSessionUserRequestMetadataAdd;
import io.tech1.framework.b2b.base.security.jwt.domain.events.EventSessionUserRequestMetadataRenew;
import io.tech1.framework.b2b.base.security.jwt.domain.functions.FunctionSessionUserRequestMetadataSave;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.domain.sessions.SessionsExpiredTable;
import io.tech1.framework.b2b.base.security.jwt.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.b2b.base.security.jwt.repositories.UsersSessionsRepository;
import io.tech1.framework.b2b.base.security.jwt.services.BaseUsersSessionsService;
import io.tech1.framework.b2b.base.security.jwt.utils.SecurityJwtTokenUtils;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.http.requests.UserAgentHeader;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;
import io.tech1.framework.domain.tuples.Tuple3;
import io.tech1.framework.domain.tuples.TupleToggle;
import io.tech1.framework.utilities.browsers.UserAgentDetailsUtility;
import io.tech1.framework.utilities.geo.facades.GeoLocationFacadeUtility;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.tech1.framework.b2b.base.security.jwt.domain.db.UserSession.ofNotPersisted;
import static io.tech1.framework.b2b.base.security.jwt.domain.db.UserSession.ofPersisted;
import static io.tech1.framework.domain.utilities.http.HttpServletRequestUtility.getClientIpAddr;
import static io.tech1.framework.domain.utilities.time.TimestampUtility.getCurrentTimestamp;
import static io.tech1.framework.domain.utilities.time.TimestampUtility.isPast;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractBaseUsersSessionsService implements BaseUsersSessionsService {

    // Publishers
    protected final SecurityJwtPublisher securityJwtPublisher;
    // Repositories
    protected final UsersSessionsRepository usersSessionsRepository;
    // Utilities
    protected final GeoLocationFacadeUtility geoLocationFacadeUtility;
    protected final SecurityJwtTokenUtils securityJwtTokenUtils;
    protected final UserAgentDetailsUtility userAgentDetailsUtility;

    @Override
    public void save(JwtUser user, JwtAccessToken accessToken, JwtRefreshToken refreshToken, HttpServletRequest httpServletRequest) {
        var username = user.username();
        var userSessionTP = this.usersSessionsRepository.isPresent(accessToken);
        var clientIpAddr = getClientIpAddr(httpServletRequest);
        var metadata = UserRequestMetadata.processing(clientIpAddr);
        var session = userSessionTP.value();
        if (userSessionTP.present()) {
            session = ofPersisted(
                    session.id(),
                    session.createdAt(),
                    session.updatedAt(),
                    session.username(),
                    session.accessToken(),
                    session.refreshToken(),
                    metadata,
                    session.metadataRenewCron(),
                    session.metadataRenewManually()
            );
        } else {
            session = ofNotPersisted(username, accessToken, refreshToken, metadata);
        }
        session = this.usersSessionsRepository.saveAs(session);
        this.securityJwtPublisher.publishSessionUserRequestMetadataAdd(
                new EventSessionUserRequestMetadataAdd(
                        username,
                        user.email(),
                        session,
                        clientIpAddr,
                        new UserAgentHeader(httpServletRequest),
                        true,
                        false
                )
        );
    }

    @Override
    public void refresh(JwtUser user, UserSession oldSession, JwtAccessToken newAccessToken, JwtRefreshToken newRefreshToken, HttpServletRequest httpServletRequest) {
        var username = user.username();
        var newSession = this.usersSessionsRepository.saveAs(ofNotPersisted(username, newAccessToken, newRefreshToken, oldSession.metadata()));
        this.usersSessionsRepository.delete(oldSession.id());
        this.securityJwtPublisher.publishSessionUserRequestMetadataAdd(
                new EventSessionUserRequestMetadataAdd(
                        username,
                        user.email(),
                        newSession,
                        getClientIpAddr(httpServletRequest),
                        new UserAgentHeader(httpServletRequest),
                        false,
                        true
                )
        );
    }

    @Override
    public UserSession saveUserRequestMetadata(EventSessionUserRequestMetadataAdd event) {
        return this.saveUserRequestMetadata(event.getSaveFunction());
    }

    @Override
    public void saveUserRequestMetadata(EventSessionUserRequestMetadataRenew event) {
        this.saveUserRequestMetadata(event.getSaveFunction());
    }

    @Override
    public UserSession saveUserRequestMetadata(FunctionSessionUserRequestMetadataSave saveFunction) {
        var geoLocation = this.geoLocationFacadeUtility.getGeoLocation(saveFunction.clientIpAddr());
        var userAgentDetails = this.userAgentDetailsUtility.getUserAgentDetails(saveFunction.userAgentHeader());
        var session = saveFunction.session();
        var sessionProcessedMetadata = ofPersisted(
                session.id(),
                session.createdAt(),
                getCurrentTimestamp(),
                session.username(),
                session.accessToken(),
                session.refreshToken(),
                UserRequestMetadata.processed(geoLocation, userAgentDetails),
                saveFunction.metadataRenewCron().enabled() ? saveFunction.metadataRenewCron().value() : session.metadataRenewCron(),
                saveFunction.metadataRenewManually().enabled() ? saveFunction.metadataRenewManually().value() : session.metadataRenewManually()
        );
        return this.usersSessionsRepository.saveAs(sessionProcessedMetadata);
    }

    @Override
    public SessionsExpiredTable getExpiredRefreshTokensSessions(Set<Username> usernames) {
        var usersSessions = this.usersSessionsRepository.findByUsernameInAsAny(usernames);
        List<Tuple3<Username, JwtRefreshToken, UserRequestMetadata>> expiredSessions = new ArrayList<>();
        Set<UserSessionId> expiredOrInvalidSessionIds = new HashSet<>();

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
    public void enableUserRequestMetadataRenewCron() {
        this.usersSessionsRepository.enableMetadataRenewCron();
    }

    @Override
    public void enableUserRequestMetadataRenewManually(UserSessionId sessionId) {
        this.usersSessionsRepository.enableMetadataRenewManually(sessionId);
    }

    @Override
    public void renewUserRequestMetadata(UserSession session, HttpServletRequest httpServletRequest) {
        if (session.isRenewRequired()) {
            this.securityJwtPublisher.publishSessionUserRequestMetadataRenew(
                    new EventSessionUserRequestMetadataRenew(
                            session.username(),
                            session,
                            getClientIpAddr(httpServletRequest),
                            new UserAgentHeader(httpServletRequest),
                            session.metadataRenewCron() ? TupleToggle.enabled(true) : TupleToggle.disabled(),
                            session.metadataRenewManually() ? TupleToggle.enabled(true) : TupleToggle.disabled()
                    )
            );
        }
    }

    @Override
    public void deleteById(UserSessionId sessionId) {
        this.usersSessionsRepository.delete(sessionId);
    }

    @Override
    public void deleteAllExceptCurrent(Username username, CookieAccessToken cookie) {
        this.usersSessionsRepository.deleteByUsernameExceptAccessToken(username, cookie);
    }

    @Override
    public void deleteAllExceptCurrentAsSuperuser(CookieAccessToken cookie) {
        this.usersSessionsRepository.deleteExceptAccessToken(cookie);
    }
}
