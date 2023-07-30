package io.tech1.framework.b2b.base.security.jwt.repositories;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbUserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;

import java.util.List;

public interface AnyDbUsersSessionsRepository {
    long deleteByIdIn(List<String> ids);

    AnyDbUserSession findByRefreshTokenAnyDb(JwtRefreshToken jwtRefreshToken);

    void deleteByRefreshToken(JwtRefreshToken jwtRefreshToken);
}
