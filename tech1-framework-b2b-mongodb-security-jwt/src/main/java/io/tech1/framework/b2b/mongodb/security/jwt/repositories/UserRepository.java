package io.tech1.framework.b2b.mongodb.security.jwt.repositories;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.domain.base.Email;
import io.tech1.framework.domain.base.Username;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.tech1.framework.domain.base.AbstractAuthority.SUPER_ADMIN;

@Repository
public interface UserRepository extends MongoRepository<DbUser, String> {
    DbUser findByEmail(Email email);
    DbUser findByUsername(Username username);
    List<DbUser> findByUsernameIn(Set<Username> usernames);
    List<DbUser> findByUsernameIn(List<Username> usernames);

    default List<DbUser> findSuperadmins() {
        var superadminAuthority = new SimpleGrantedAuthority(SUPER_ADMIN);
        return this.findAll().stream()
                .filter(user -> user.getAuthorities().contains(superadminAuthority))
                .collect(Collectors.toList());
    }
}
