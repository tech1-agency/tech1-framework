package io.tech1.framework.iam.server.postgres.services;

import io.tech1.framework.iam.domain.postgres.db.PostgresDbUser;

import java.util.List;

public interface UsersService {
    List<PostgresDbUser> findAll();
}
