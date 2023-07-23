package io.tech1.framework.b2b.mongodb.security.jwt.repositories;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUserSession;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.base.Username;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import static java.util.Objects.nonNull;

@Repository
public interface UserSessionRepository extends MongoRepository<DbUserSession, String> {
    // ================================================================================================================
    // Spring Data
    // ================================================================================================================
    List<DbUserSession> findByUsername(Username username);
    List<DbUserSession> findByUsernameIn(Set<Username> usernames);
    Long deleteByIdIn(List<String> ids);

    default boolean isPresent(JwtRefreshToken jwtRefreshToken) {
        return nonNull(this.findByRefreshToken(jwtRefreshToken));
    }

    default DbUserSession findByRefreshToken(JwtRefreshToken jwtRefreshToken) {
        return this.findById(jwtRefreshToken.value()).orElse(null);
    }

    default void deleteByRefreshToken(JwtRefreshToken jwtRefreshToken) {
        this.deleteById(jwtRefreshToken.value());
    }

    // ================================================================================================================
    // Queries
    // ================================================================================================================
    @Query(value = "{ 'username': { '$in': ?0}}")
    List<DbUserSession> findByUsernames(List<Username> usernames);

    @Query(value = "{ 'username': { '$in': ?0}}", delete = true)
    void deleteByUsernames(List<Username> usernames);
}
