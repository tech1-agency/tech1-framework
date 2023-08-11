package io.tech1.framework.domain.exceptions.cookie;

import io.tech1.framework.domain.base.Username;

public class CookieAccessTokenDbNotFoundException extends Exception {

    public CookieAccessTokenDbNotFoundException(Username username) {
        super("JWT access token is not present in database. Username: " + username);
    }
}
