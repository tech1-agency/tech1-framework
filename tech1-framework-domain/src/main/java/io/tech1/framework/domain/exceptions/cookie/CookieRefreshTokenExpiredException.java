package io.tech1.framework.domain.exceptions.cookie;

import io.tech1.framework.domain.base.Username;

public class CookieRefreshTokenExpiredException extends Exception {

    public CookieRefreshTokenExpiredException(Username username) {
        super("JWT refresh token is expired. Username: " + username);
    }
}
