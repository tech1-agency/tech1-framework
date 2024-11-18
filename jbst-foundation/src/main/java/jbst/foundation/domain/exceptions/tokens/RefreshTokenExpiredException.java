package jbst.foundation.domain.exceptions.tokens;

import jbst.foundation.domain.base.Username;

public class RefreshTokenExpiredException extends Exception {

    public RefreshTokenExpiredException(Username username) {
        super("JWT refresh token is expired. Username: " + username);
    }
}
