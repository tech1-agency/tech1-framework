package io.tech1.framework.b2b.mongodb.security.jwt.repositories;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbUserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseSuperadminSessionsTable;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbUsersSessionsRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUserSession;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.tuples.TuplePresence;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static io.tech1.framework.b2b.base.security.jwt.comparators.SecurityJwtComparators.*;
import static io.tech1.framework.domain.tuples.TuplePresence.present;

@Repository
public interface MongoUsersSessionsRepository extends MongoRepository<MongoDbUserSession, String>, AnyDbUsersSessionsRepository {
    // ================================================================================================================
    // Any
    // ================================================================================================================
    default TuplePresence<AnyDbUserSession> isPresent(UserSessionId sessionId) {
        return this.findById(sessionId.value())
                .map(entity -> present(entity.anyDbUserSession()))
                .orElseGet(TuplePresence::absent);
    }

    default TuplePresence<AnyDbUserSession> isPresent(JwtAccessToken accessToken) {
        return this.findByAccessToken(accessToken)
                .map(entity -> present(entity.anyDbUserSession()))
                .orElseGet(TuplePresence::absent);
    }

    default TuplePresence<AnyDbUserSession> isPresent(JwtRefreshToken refreshToken) {
        return this.findByRefreshToken(refreshToken)
                .map(entity -> present(entity.anyDbUserSession()))
                .orElseGet(TuplePresence::absent);
    }

    default List<ResponseUserSession2> getUsersSessionsTable(Username username, CookieAccessToken cookie) {
        return this.findByUsername(username).stream()
                .map(session -> session.responseUserSession2(cookie))
                .sorted(USERS_SESSIONS)
                .collect(Collectors.toList());
    }

    default ResponseSuperadminSessionsTable getSessionsTable(Set<JwtAccessToken> activeAccessTokens, CookieAccessToken cookie) {
        var sessions = this.findAll();

        List<ResponseUserSession2> activeSessions = new ArrayList<>();
        List<ResponseUserSession2> inactiveSessions = new ArrayList<>();

        sessions.forEach(session -> {
            var session2 = session.responseUserSession2(cookie);
            if (activeAccessTokens.contains(session.getAccessToken())) {
                activeSessions.add(session2);
            } else {
                inactiveSessions.add(session2);
            }
        });

        activeSessions.sort(ACTIVE_SESSIONS_AS_SUPERADMIN);
        inactiveSessions.sort(INACTIVE_SESSIONS_AS_SUPERADMIN);

        return new ResponseSuperadminSessionsTable(activeSessions, inactiveSessions);
    }

    default List<AnyDbUserSession> findByUsernameInAsAny(Set<Username> usernames) {
        return this.findByUsernameIn(usernames).stream()
                .map(MongoDbUserSession::anyDbUserSession)
                .collect(Collectors.toList());
    }

    default UserSessionId saveAs(AnyDbUserSession userSession) {
        var entity = this.save(new MongoDbUserSession(userSession));
        return entity.userSessionId();
    }

    default void delete(UserSessionId sessionId) {
        this.deleteById(sessionId.value());
    }

    default long deleteByUsersSessionsIds(List<UserSessionId> sessionsIds) {
        return this.deleteByIdIn(sessionsIds.stream().map(UserSessionId::value).toList());
    }

    default void deleteByUsernameExceptAccessToken(Username username, CookieAccessToken cookie) {
        this.deleteByUsernameExceptAccessToken(username, cookie.getJwtAccessToken());
    }

    default void deleteExceptAccessToken(CookieAccessToken cookie) {
        this.deleteExceptToken(cookie.getJwtAccessToken());
    }

    // ================================================================================================================
    // Spring Data
    // ================================================================================================================
    Optional<MongoDbUserSession> findByAccessToken(JwtAccessToken accessToken);
    Optional<MongoDbUserSession> findByRefreshToken(JwtRefreshToken refreshToken);
    List<MongoDbUserSession> findByUsername(Username username);
    List<MongoDbUserSession> findByUsernameIn(Set<Username> usernames);

    long deleteByIdIn(List<String> ids);

    // ================================================================================================================
    // Queries
    // ================================================================================================================
    @Query(value = "{ 'username': { '$in': ?0}}", delete = true)
    void deleteByUsernames(Set<Username> usernames);

    @Query(value = "{ 'username': ?0, 'accessToken': { $ne: ?1 } }", delete = true)
    void deleteByUsernameExceptAccessToken(Username username, JwtAccessToken accessToken);

    @Query(value = "{ 'accessToken': { $ne: ?0 } }", delete = true)
    void deleteExceptToken(JwtAccessToken accessToken);
}
