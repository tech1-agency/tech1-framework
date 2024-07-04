package io.tech1.framework.iam.server.repositories;

import io.tech1.framework.iam.server.domain.db.PostgresDbAnything;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostgresAnythingRepository extends JpaRepository<PostgresDbAnything, String> {

}
