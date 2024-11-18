package tech1.framework.foundation.domain.exceptions.tokens;

import tech1.framework.foundation.domain.base.Username;

public class AccessTokenExpiredException extends Exception {

    public AccessTokenExpiredException(Username username) {
        super("JWT access token is expired. Username: " + username);
    }
}
