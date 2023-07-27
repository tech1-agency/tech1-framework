package io.tech1.framework.b2b.postgres.security.jwt.repositories;

import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUser;
import io.tech1.framework.domain.base.Username;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostgresUserRepository extends JpaRepository<PostgresDbUser, String> {
    // ================================================================================================================
    // Spring Data
    // ================================================================================================================
    PostgresDbUser findByUsername(Username username);
}
