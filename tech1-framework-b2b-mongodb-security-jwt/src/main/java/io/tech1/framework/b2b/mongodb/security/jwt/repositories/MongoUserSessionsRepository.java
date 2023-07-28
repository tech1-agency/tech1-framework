package io.tech1.framework.b2b.mongodb.security.jwt.repositories;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.base.Username;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.entityNotFound;
import static java.util.Objects.nonNull;

@Repository
public interface MongoUserSessionsRepository extends MongoRepository<MongoDbUserSession, String> {
    // ================================================================================================================
    // Spring Data
    // ================================================================================================================
    List<MongoDbUserSession> findByUsername(Username username);
    List<MongoDbUserSession> findByUsernameIn(Set<Username> usernames);
    Long deleteByIdIn(List<String> ids);

    default MongoDbUserSession getById(String sessionId) {
        return this.findById(sessionId).orElse(null);
    }

    default MongoDbUserSession requirePresence(String sessionId) {
        var session = this.getById(sessionId);
        assertNonNullOrThrow(session, entityNotFound("Session", sessionId));
        return session;
    }

    default boolean isPresent(JwtRefreshToken jwtRefreshToken) {
        return nonNull(this.findByRefreshToken(jwtRefreshToken));
    }

    default MongoDbUserSession findByRefreshToken(JwtRefreshToken jwtRefreshToken) {
        return this.findById(jwtRefreshToken.value()).orElse(null);
    }

    default void deleteByRefreshToken(JwtRefreshToken jwtRefreshToken) {
        this.deleteById(jwtRefreshToken.value());
    }

    // ================================================================================================================
    // Queries
    // ================================================================================================================
    @Query(value = "{ 'username': { '$in': ?0}}")
    List<MongoDbUserSession> findByUsernames(List<Username> usernames);

    @Query(value = "{ 'username': { '$in': ?0}}", delete = true)
    void deleteByUsernames(List<Username> usernames);
}
