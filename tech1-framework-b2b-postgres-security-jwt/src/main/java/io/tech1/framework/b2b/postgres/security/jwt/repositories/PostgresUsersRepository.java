package io.tech1.framework.b2b.postgres.security.jwt.repositories;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.base.security.jwt.repositories.AnyDbUsersRepository;
import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUser;
import io.tech1.framework.b2b.postgres.security.jwt.domain.projections.PostgresDbUserProjection1;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.tech1.framework.b2b.base.security.jwt.constants.SecurityJwtConstants.SUPERADMIN;
import static io.tech1.framework.b2b.postgres.security.jwt.constants.PostgreTablesConstants.USERS;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.entityNotFound;
import static java.util.Objects.nonNull;

@SuppressWarnings("JpaQlInspection")
public interface PostgresUsersRepository extends JpaRepository<PostgresDbUser, String>, AnyDbUsersRepository {
    // ================================================================================================================
    // Any
    // ================================================================================================================
    default JwtUser loadUserByUsername(Username username) throws UsernameNotFoundException {
        var user = this.findByUsername(username);
        if (nonNull(user)) {
            return user.asJwtUser();
        } else {
            throw new UsernameNotFoundException(entityNotFound("Username", username.identifier()));
        }
    }

    default JwtUser findByUsernameAsJwtUser(Username username) {
        return this.findByUsername(username).asJwtUser();
    }

    default JwtUser findByEmailAsJwtUser(Email email) {
        return this.findByEmail(email).asJwtUser();
    }

    // ================================================================================================================
    // Spring Data
    // ================================================================================================================
    PostgresDbUser findByEmail(Email email);
    PostgresDbUser findByUsername(Username username);
    List<PostgresDbUser> findByUsernameIn(Set<Username> usernames);
    List<PostgresDbUser> findByUsernameIn(List<Username> usernames);

    // ================================================================================================================
    // Queries
    // ================================================================================================================
    @Query(value = "SELECT * FROM " + USERS + " u WHERE u.authorities LIKE %:authority%", nativeQuery = true)
    List<PostgresDbUser> findByAuthority(@Param("authority") SimpleGrantedAuthority authority);

    @Query(value = "SELECT u.username FROM " + USERS + " u WHERE u.authorities LIKE %:authority%", nativeQuery = true)
    List<PostgresDbUserProjection1> findByAuthorityProjectionUsernames(@Param("authority") SimpleGrantedAuthority authority);

    @Query(value = "SELECT * FROM " + USERS + " u WHERE u.authorities NOT LIKE %:authority%", nativeQuery = true)
    List<PostgresDbUser> findByAuthorityNotEqual(@Param("authority") SimpleGrantedAuthority authority);

    @Query(value = "SELECT * FROM " + USERS + " u WHERE u.authorities NOT LIKE %:authority%", nativeQuery = true)
    List<PostgresDbUserProjection1> findByAuthorityNotEqualProjectionUsernames(@Param("authority") SimpleGrantedAuthority authority);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM " + USERS + " u WHERE u.authorities LIKE %:authority%", nativeQuery = true)
    void deleteByAuthority(@Param("authority") SimpleGrantedAuthority authority);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM " + USERS + " u WHERE u.authorities NOT LIKE %:authority%", nativeQuery = true)
    void deleteByAuthorityNotEqual(@Param("authority") SimpleGrantedAuthority authority);

    default List<PostgresDbUser> findByAuthoritySuperadmin() {
        return this.findByAuthority(SUPERADMIN);
    }

    default List<Username> findSuperadminsUsernames() {
        return this.findByAuthorityProjectionUsernames(SUPERADMIN).stream().map(PostgresDbUserProjection1::getAsUsername).collect(Collectors.toList());
    }

    default List<PostgresDbUser> findByAuthorityNotSuperadmin() {
        return this.findByAuthorityNotEqual(SUPERADMIN);
    }

    default List<Username> findNotSuperadminsUsernames() {
        return this.findByAuthorityNotEqualProjectionUsernames(SUPERADMIN).stream().map(PostgresDbUserProjection1::getAsUsername).collect(Collectors.toList());
    }

    @Transactional
    default void deleteByAuthoritySuperadmin() {
        this.deleteByAuthority(SUPERADMIN);
    }

    @Transactional
    default void deleteByAuthorityNotSuperadmin() {
        this.deleteByAuthorityNotEqual(SUPERADMIN);
    }
}
