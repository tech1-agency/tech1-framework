package tech1.framework.iam.server.postgres.repositories;

import tech1.framework.iam.server.postgres.domain.db.PostgresDbAnything;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostgresAnythingRepository extends JpaRepository<PostgresDbAnything, String> {

}
