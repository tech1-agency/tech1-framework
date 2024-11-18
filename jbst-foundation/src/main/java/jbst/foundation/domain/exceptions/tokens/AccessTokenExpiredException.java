package jbst.foundation.domain.exceptions.tokens;

import jbst.foundation.domain.base.Username;

public class AccessTokenExpiredException extends Exception {

    public AccessTokenExpiredException(Username username) {
        super("JWT access token is expired. Username: " + username);
    }
}
