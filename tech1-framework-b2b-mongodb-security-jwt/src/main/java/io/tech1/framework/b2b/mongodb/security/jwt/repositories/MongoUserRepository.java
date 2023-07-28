package io.tech1.framework.b2b.mongodb.security.jwt.repositories;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.MongoDbUser;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Username;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.base.AbstractAuthority.SUPER_ADMIN;

@Repository
public interface MongoUserRepository extends MongoRepository<MongoDbUser, String> {
    // ================================================================================================================
    // Constants
    // ================================================================================================================
    SimpleGrantedAuthority SUPERADMIN = new SimpleGrantedAuthority(SUPER_ADMIN);

    // ================================================================================================================
    // Spring Data
    // ================================================================================================================
    MongoDbUser findByEmail(Email email);
    MongoDbUser findByUsername(Username username);
    List<MongoDbUser> findByUsernameIn(Set<Username> usernames);
    List<MongoDbUser> findByUsernameIn(List<Username> usernames);

    // ================================================================================================================
    // Queries
    // ================================================================================================================
    @Query(value = "{ 'authorities': ?0}")
    List<MongoDbUser> findByAuthority(SimpleGrantedAuthority authority);

    @Query(value = "{ 'authorities': ?0}", fields = "{ 'id': 0, 'username' : 1}")
    List<MongoDbUser> findByAuthorityProjectionUsernames(SimpleGrantedAuthority authority);

    @Query(value = "{ 'authorities': { '$ne': ?0}}")
    List<MongoDbUser> findByAuthorityNotEqual(SimpleGrantedAuthority authority);

    @Query(value = "{ 'authorities': { '$ne': ?0}}", fields = "{ 'id': 0, 'username' : 1}")
    List<MongoDbUser> findByAuthorityNotEqualProjectionUsernames(SimpleGrantedAuthority authority);

    @Query(value = "{ 'authorities': ?0}", delete = true)
    void deleteByAuthority(SimpleGrantedAuthority authority);

    @Query(value = "{ 'authorities': { '$ne': ?0}}", delete = true)
    void deleteByAuthorityNotEqual(SimpleGrantedAuthority authority);

    default List<MongoDbUser> findByAuthoritySuperadmin() {
        return this.findByAuthority(SUPERADMIN);
    }

    default List<Username> findSuperadminsUsernames() {
        return this.findByAuthorityProjectionUsernames(SUPERADMIN).stream().map(MongoDbUser::getUsername).collect(Collectors.toList());
    }

    default List<MongoDbUser> findByAuthorityNotSuperadmin() {
        return this.findByAuthorityNotEqual(SUPERADMIN);
    }

    default List<Username> findNotSuperadminsUsernames() {
        return this.findByAuthorityNotEqualProjectionUsernames(SUPERADMIN).stream().map(MongoDbUser::getUsername).collect(Collectors.toList());
    }

    default void deleteByAuthoritySuperadmin() {
        this.deleteByAuthority(SUPERADMIN);
    }

    default void deleteByAuthorityNotSuperadmin() {
        this.deleteByAuthorityNotEqual(SUPERADMIN);
    }
}
