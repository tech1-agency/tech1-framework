package io.tech1.framework.b2b.postgres.security.jwt.repositories;

import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbInvitationCode;
import io.tech1.framework.domain.base.Username;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@SuppressWarnings("JpaQlInspection")
public interface PostgresInvitationCodesRepository extends JpaRepository<PostgresDbInvitationCode, String> {
    // ================================================================================================================
    // Spring Data
    // ================================================================================================================
    List<PostgresDbInvitationCode> findByOwner(Username username);
}
