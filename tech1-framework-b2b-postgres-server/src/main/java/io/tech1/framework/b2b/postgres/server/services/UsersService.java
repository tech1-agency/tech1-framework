package io.tech1.framework.b2b.postgres.server.services;

import io.tech1.framework.b2b.postgres.security.jwt.domain.db.PostgresDbUser;

import java.util.List;

public interface UsersService {
    List<PostgresDbUser> findAll();
}
