package io.tech1.framework.b2b.postgres.server.repositories;

import io.tech1.framework.b2b.postgres.server.domain.db.PostgresDbAnything;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostgresAnythingRepository extends JpaRepository<PostgresDbAnything, String> {

}
