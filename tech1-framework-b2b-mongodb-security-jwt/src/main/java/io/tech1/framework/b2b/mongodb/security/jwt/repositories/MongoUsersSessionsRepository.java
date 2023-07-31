package io.tech1.framework.b2b.mongodb.security.jwt.repositories;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbUserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbUsersSessionsRepository;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUserSession;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.tuples.Tuple2;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.entityNotFound;
import static java.util.Objects.nonNull;

@Repository
public interface MongoUsersSessionsRepository extends MongoRepository<MongoDbUserSession, String>, AnyDbUsersSessionsRepository {
    // ================================================================================================================
    // Any
    // ================================================================================================================
    default boolean isPresent(JwtRefreshToken jwtRefreshToken) {
        return nonNull(this.findByRefreshTokenAsAny(jwtRefreshToken));
    }

    default AnyDbUserSession requirePresence(UserSessionId sessionId) {
        var session = this.getById(sessionId);
        assertNonNullOrThrow(session, entityNotFound("Session", sessionId.value()));
        return session.anyDbUserSession();
    }

    default List<ResponseUserSession2> findByUsernameAndCookieAsSession2(Username username, CookieRefreshToken cookie) {
        return this.findByUsername(username).stream()
                .map(session -> session.responseUserSession2(cookie))
                .collect(Collectors.toList());
    }

    default List<Tuple2<ResponseUserSession2, JwtRefreshToken>> findAllByCookieAsSession2(CookieRefreshToken cookie) {
        return this.findAll().stream()
                .map(session ->
                        new Tuple2<>(
                                ResponseUserSession2.of(
                                        session.getUsername(),
                                        session.getRequestMetadata(),
                                        session.getJwtRefreshToken(),
                                        cookie
                                ),
                                session.getJwtRefreshToken()
                        )
                )
                .collect(Collectors.toList());
    }

    default List<AnyDbUserSession> findByUsernameInAsAny(Set<Username> usernames) {
        return this.findByUsernameIn(usernames).stream()
                .map(MongoDbUserSession::anyDbUserSession)
                .collect(Collectors.toList());
    }

    default AnyDbUserSession findByRefreshTokenAsAny(JwtRefreshToken jwtRefreshToken) {
        return this.findById(jwtRefreshToken.value()).map(MongoDbUserSession::anyDbUserSession).orElse(null);
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

    default void deleteByRefreshToken(JwtRefreshToken jwtRefreshToken) {
        this.deleteById(jwtRefreshToken.value());
    }

    // ================================================================================================================
    // Spring Data
    // ================================================================================================================
    List<MongoDbUserSession> findByUsername(Username username);
    List<MongoDbUserSession> findByUsernameIn(Set<Username> usernames);

    default MongoDbUserSession getById(UserSessionId sessionId) {
        return this.findById(sessionId.value()).orElse(null);
    }

    @Deprecated
    default MongoDbUserSession findByRefreshToken(JwtRefreshToken jwtRefreshToken) {
        return this.findById(jwtRefreshToken.value()).orElse(null);
    }

    long deleteByIdIn(List<String> ids);

    // ================================================================================================================
    // Queries
    // ================================================================================================================
    @Query(value = "{ 'username': { '$in': ?0}}", delete = true)
    void deleteByUsernames(Set<Username> usernames);
}
