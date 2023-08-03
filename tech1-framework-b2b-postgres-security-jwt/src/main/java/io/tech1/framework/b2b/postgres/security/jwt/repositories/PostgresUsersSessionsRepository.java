package io.tech1.framework.b2b.postgres.security.jwt.repositories;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbUserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseSuperadminSessionsTable;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbUsersSessionsRepository;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUserSession;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.tuples.TuplePresence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static io.tech1.framework.b2b.base.security.jwt.comparators.SecurityJwtComparators.*;
import static io.tech1.framework.domain.tuples.TuplePresence.present;

@SuppressWarnings("JpaQlInspection")
public interface PostgresUsersSessionsRepository extends JpaRepository<PostgresDbUserSession, String>, AnyDbUsersSessionsRepository {
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
                .map(PostgresDbUserSession::anyDbUserSession)
                .collect(Collectors.toList());
    }

    default void delete(UserSessionId sessionId) {
        this.deleteById(sessionId.value());
    }

    @Transactional
    default long delete(Set<UserSessionId> sessionsIds) {
        return this.deleteByIdIn(sessionsIds.stream().map(UserSessionId::value).toList());
    }

    @Transactional
    default void deleteByUsernameExceptAccessToken(Username username, CookieAccessToken cookie) {
        this.deleteByUsernameExceptAccessToken(username, cookie.getJwtAccessToken());
    }

    @Transactional
    default void deleteExceptAccessToken(CookieAccessToken cookie) {
        this.deleteExceptToken(cookie.getJwtAccessToken());
    }

    // TODO [YY] return AnyDbUserSession
    default UserSessionId saveAs(AnyDbUserSession userSession) {
        var entity = this.save(new PostgresDbUserSession(userSession));
        return entity.userSessionId();
    }

    // ================================================================================================================
    // Spring Data
    // ================================================================================================================
    Optional<PostgresDbUserSession> findByAccessToken(JwtAccessToken accessToken);
    Optional<PostgresDbUserSession> findByRefreshToken(JwtRefreshToken refreshToken);
    List<PostgresDbUserSession> findByUsername(Username username);
    List<PostgresDbUserSession> findByUsernameIn(Set<Username> usernames);

    @Transactional
    @Modifying
    long deleteByIdIn(List<String> ids);

    // ================================================================================================================
    // Queries
    // ================================================================================================================
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM PostgresDbUserSession s WHERE s.username IN :usernames")
    void deleteByUsernames(@Param("usernames") Set<Username> usernames);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM PostgresDbUserSession s WHERE s.username = :username AND s.accessToken != :accessToken")
    void deleteByUsernameExceptAccessToken(@Param("username") Username username, @Param("accessToken") JwtAccessToken accessToken);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM PostgresDbUserSession s WHERE s.accessToken != :accessToken")
    void deleteExceptToken(@Param("accessToken") JwtAccessToken accessToken);
}
