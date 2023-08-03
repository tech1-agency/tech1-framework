package io.tech1.framework.b2b.postgres.security.jwt.repositories;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbUserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtAccessToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbUsersSessionsRepository;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUserSession;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.tuples.Tuple2;
import io.tech1.framework.domain.tuples.TuplePresence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    default boolean isPresentByAccessToken(JwtAccessToken accessToken) {
        return this.findByAccessToken(accessToken).isPresent();
    }

    default boolean isPresentByRefreshToken(JwtRefreshToken refreshToken) {
        return this.findByRefreshToken(refreshToken).isPresent();
    }

    default AnyDbUserSession getById(UserSessionId sessionId) {
        return this.findById(sessionId.value()).map(PostgresDbUserSession::anyDbUserSession).orElse(null);
    }

    default List<ResponseUserSession2> findByUsernameAndCookieAsSession2(Username username, CookieAccessToken cookie) {
        return this.findByUsername(username).stream()
                .map(session -> session.responseUserSession2(cookie))
                .collect(Collectors.toList());
    }

    default List<Tuple2<ResponseUserSession2, JwtAccessToken>> findAllByCookieAsSession2(CookieAccessToken cookie) {
        return this.findAll().stream()
                .map(session -> new Tuple2<>(session.responseUserSession2(cookie), session.getAccessToken()))
                .collect(Collectors.toList());
    }

    default List<AnyDbUserSession> findByUsernameInAsAny(Set<Username> usernames) {
        return this.findByUsernameIn(usernames).stream()
                .map(PostgresDbUserSession::anyDbUserSession)
                .collect(Collectors.toList());
    }

    default AnyDbUserSession findByAccessTokenAsAny(JwtAccessToken accessToken) {
        return this.findByAccessToken(accessToken).map(PostgresDbUserSession::anyDbUserSession).orElse(null);
    }

    default AnyDbUserSession findByRefreshTokenAsAny(JwtRefreshToken refreshToken) {
        return this.findByRefreshToken(refreshToken).map(PostgresDbUserSession::anyDbUserSession).orElse(null);
    }

    default UserSessionId saveAs(AnyDbUserSession userSession) {
        var entity = this.save(new PostgresDbUserSession(userSession));
        return entity.userSessionId();
    }

    default void delete(UserSessionId sessionId) {
        this.deleteById(sessionId.value());
    }

    default long deleteByUsersSessionsIds(List<UserSessionId> sessionsIds) {
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

    // ================================================================================================================
    // Spring Data
    // ================================================================================================================
    List<PostgresDbUserSession> findByUsername(Username username);
    List<PostgresDbUserSession> findByUsernameIn(Set<Username> usernames);
    Optional<PostgresDbUserSession> findByAccessToken(JwtAccessToken accessToken);
    Optional<PostgresDbUserSession> findByRefreshToken(JwtRefreshToken refreshToken);

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
