package io.tech1.framework.b2b.base.security.jwt.repositories;

import io.tech1.framework.b2b.base.security.jwt.domain.db.AnyDbUserSession;
import io.tech1.framework.b2b.base.security.jwt.domain.dto.responses.ResponseUserSession2;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.CookieRefreshToken;
import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtRefreshToken;
import io.tech1.framework.domain.base.Username;

import java.util.List;

public interface AnyDbUsersSessionsRepository {
    List<ResponseUserSession2> getSessions(Username username, CookieRefreshToken cookie);

    AnyDbUserSession findByRefreshTokenAnyDb(JwtRefreshToken jwtRefreshToken);

    long deleteByIdIn(List<String> ids);

    void deleteByRefreshToken(JwtRefreshToken jwtRefreshToken);
}
