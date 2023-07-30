package io.tech1.framework.b2b.postgres.security.jwt.repositories;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbUserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.identifiers.UserSessionId;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUserSession;
import io.tech1.framework.domain.base.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.entityNotFound;
import static java.util.Objects.nonNull;

@SuppressWarnings("JpaQlInspection")
public interface PostgresUsersSessionsRepository extends JpaRepository<PostgresDbUserSession, String> {
    // ================================================================================================================
    // Spring Data
    // ================================================================================================================
    List<PostgresDbUserSession> findByUsername(Username username);
    List<PostgresDbUserSession> findByUsernameIn(Set<Username> usernames);
    Long deleteByIdIn(List<String> ids);

    default PostgresDbUserSession getById(UserSessionId sessionId) {
        return this.findById(sessionId.value()).orElse(null);
    }

    default PostgresDbUserSession requirePresence(UserSessionId sessionId) {
        var session = this.getById(sessionId);
        assertNonNullOrThrow(session, entityNotFound("Session", sessionId.value()));
        return session;
    }

    default boolean isPresent(JwtRefreshToken jwtRefreshToken) {
        return nonNull(this.findByRefreshToken(jwtRefreshToken));
    }

    default PostgresDbUserSession findByRefreshToken(JwtRefreshToken jwtRefreshToken) {
        return this.findById(jwtRefreshToken.value()).orElse(null);
    }

    default AnyDbUserSession findByRefreshTokenAnyDb(JwtRefreshToken jwtRefreshToken) {
        return this.findById(jwtRefreshToken.value()).map(PostgresDbUserSession::anyDbUserSession).orElse(null);
    }

    default void deleteByRefreshToken(JwtRefreshToken jwtRefreshToken) {
        this.deleteById(jwtRefreshToken.value());
    }

    // ================================================================================================================
    // Queries
    // ================================================================================================================
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM PostgresDbUserSession s WHERE s.username IN :usernames")
    void deleteByUsernames(@Param("usernames") Set<Username> usernames);
}
