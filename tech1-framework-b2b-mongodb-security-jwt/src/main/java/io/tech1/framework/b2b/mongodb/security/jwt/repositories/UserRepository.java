package io.tech1.framework.b2b.mongodb.security.jwt.repositories;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.domain.base.Username;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends MongoRepository<DbUser, String> {
    DbUser findByEmail(String email);
    DbUser findByUsername(Username username);
    List<DbUser> findByUsernameIn(Set<Username> usernames);
    List<DbUser> findByUsernameIn(List<Username> usernames);
}
