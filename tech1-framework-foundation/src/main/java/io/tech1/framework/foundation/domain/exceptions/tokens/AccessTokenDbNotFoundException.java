package io.tech1.framework.foundation.domain.exceptions.tokens;

import io.tech1.framework.foundation.domain.base.Username;

public class AccessTokenDbNotFoundException extends Exception {

    public AccessTokenDbNotFoundException(Username username) {
        super("JWT access token is not present in database. Username: " + username);
    }
}
