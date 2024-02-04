package io.tech1.framework.domain.exceptions.tokens;

import io.tech1.framework.domain.base.Username;

public class CookieRefreshTokenDbNotFoundException extends Exception {

    public CookieRefreshTokenDbNotFoundException(Username username) {
        super("JWT refresh token is not present in database. Username: " + username);
    }
}
