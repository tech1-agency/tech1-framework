package io.tech1.framework.b2b.mongodb.security.jwt.assistants.core;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.db.DbUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseUserSessionsTable;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.security.CurrentClientUser;
import io.tech1.framework.domain.base.Username;
import io.tech1.framework.domain.exceptions.cookie.CookieRefreshTokenNotFoundException;

import javax.servlet.http.HttpServletRequest;

public interface CurrentSessionAssistant {
    Username getCurrentUsername();
    String getCurrentUserId();
    DbUser getCurrentDbUser();
    JwtUser getCurrentJwtUser();
    CurrentClientUser getCurrentClientUser();
    ResponseUserSessionsTable getCurrentUserDbSessionsTable(HttpServletRequest httpServletRequest) throws CookieRefreshTokenNotFoundException;
}
