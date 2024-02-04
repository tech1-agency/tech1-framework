package io.tech1.framework.domain.exceptions.tokens;

import io.tech1.framework.domain.base.Username;

public class CookieAccessTokenExpiredException extends Exception {

    public CookieAccessTokenExpiredException(Username username) {
        super("JWT access token is expired. Username: " + username);
    }
}
