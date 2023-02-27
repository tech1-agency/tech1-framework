package io.tech1.framework.b2b.mongodb.security.jwt.repositories;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Username;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import static io.tech1.framework.domain.base.AbstractAuthority.SUPER_ADMIN;

@Repository
public interface UserRepository extends MongoRepository<DbUser, String> {
    DbUser findByEmail(Email email);
    DbUser findByUsername(Username username);
    List<DbUser> findByUsernameIn(Set<Username> usernames);
    List<DbUser> findByUsernameIn(List<Username> usernames);

    // ================================================================================================================
    // Queries
    // ================================================================================================================
    @Query(value = "{ 'authorities': ?0}")
    List<DbUser> findByAuthority(SimpleGrantedAuthority authority);

    @Query(value = "{ 'authorities': ?0}", fields = "{ 'id': 0, 'username' : 1}")
    List<DbUser> findByAuthorityProjectionUsernames(SimpleGrantedAuthority authority);

    @Query(value = "{ 'authorities': { '$ne': ?0}}")
    List<DbUser> findByAuthorityNotEqual(SimpleGrantedAuthority authority);

    @Query(value = "{ 'authorities': { '$ne': ?0}}", fields = "{ 'id': 0, 'username' : 1}")
    List<DbUser> findByAuthorityNotEqualProjectionUsernames(SimpleGrantedAuthority authority);

    default List<DbUser> findByAuthoritySuperadmin() {
        return this.findByAuthority(new SimpleGrantedAuthority(SUPER_ADMIN));
    }

    default List<DbUser> findByAuthorityNotSuperadmin() {
        return this.findByAuthorityNotEqual(new SimpleGrantedAuthority(SUPER_ADMIN));
    }
}
