package io.tech1.framework.b2b.mongodb.server.services;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;

import java.util.List;

public interface UserService {
    List<DbUser> findAll();
}
