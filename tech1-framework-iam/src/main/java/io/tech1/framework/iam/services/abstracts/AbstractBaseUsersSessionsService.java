package io.tech1.framework.iam.services.abstracts;

import tech1.framework.foundation.domain.base.Username;
import tech1.framework.foundation.domain.http.requests.UserAgentHeader;
import tech1.framework.foundation.domain.http.requests.UserRequestMetadata;
import tech1.framework.foundation.domain.tuples.Tuple3;
import tech1.framework.foundation.domain.tuples.TupleToggle;
import tech1.framework.foundation.utils.UserMetadataUtils;
import io.tech1.framework.iam.domain.db.UserSession;
import io.tech1.framework.iam.domain.events.EventSessionUserRequestMetadataAdd;
import io.tech1.framework.iam.domain.events.EventSessionUserRequestMetadataRenew;
import io.tech1.framework.iam.domain.functions.FunctionSessionUserRequestMetadataSave;
import io.tech1.framework.iam.domain.identifiers.UserSessionId;
import io.tech1.framework.iam.domain.jwt.JwtAccessToken;
import io.tech1.framework.iam.domain.jwt.JwtRefreshToken;
import io.tech1.framework.iam.domain.jwt.JwtUser;
import io.tech1.framework.iam.domain.jwt.RequestAccessToken;
import io.tech1.framework.iam.domain.sessions.SessionsExpiredTable;
import io.tech1.framework.iam.events.publishers.SecurityJwtPublisher;
import io.tech1.framework.iam.repositories.UsersSessionsRepository;
import io.tech1.framework.iam.services.BaseUsersSessionsService;
import io.tech1.framework.iam.utils.SecurityJwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static tech1.framework.foundation.utilities.exceptions.ExceptionsMessagesUtility.entityAccessDenied;
import static tech1.framework.foundation.utilities.http.HttpServletRequestUtility.getClientIpAddr;
import static tech1.framework.foundation.utilities.time.TimestampUtility.getCurrentTimestamp;
import static tech1.framework.foundation.utilities.time.TimestampUtility.isPast;
import static io.tech1.framework.iam.domain.db.UserSession.ofNotPersisted;
import static io.tech1.framework.iam.domain.db.UserSession.ofPersisted;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractBaseUsersSessionsService implements BaseUsersSessionsService {

    // Publishers
    protected final SecurityJwtPublisher securityJwtPublisher;
    // Repositories
    protected final UsersSessionsRepository usersSessionsRepository;
    // Utils
    protected final UserMetadataUtils userMetadataUtils;
    // Utilities
    protected final SecurityJwtTokenUtils securityJwtTokenUtils;

    @Override
    public void assertAccess(Username username, UserSessionId sessionId) {
        var tuplePresence = this.usersSessionsRepository.isPresent(sessionId, username);
        if (!tuplePresence.present()) {
            throw new AccessDeniedException(entityAccessDenied("Session", sessionId.value()));
        }
    }

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
        var session = saveFunction.session();
        var sessionProcessedMetadata = ofPersisted(
                session.id(),
                session.createdAt(),
                getCurrentTimestamp(),
                session.username(),
                session.accessToken(),
                session.refreshToken(),
                this.userMetadataUtils.getUserRequestMetadataProcessed(saveFunction.clientIpAddr(), saveFunction.userAgentHeader()),
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
    public void deleteAllExceptCurrent(Username username, RequestAccessToken requestAccessToken) {
        this.usersSessionsRepository.deleteByUsernameExceptAccessToken(username, requestAccessToken);
    }

    @Override
    public void deleteAllExceptCurrentAsSuperuser(RequestAccessToken requestAccessToken) {
        this.usersSessionsRepository.deleteExceptAccessToken(requestAccessToken);
    }
}
