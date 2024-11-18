package jbst.foundation.domain.exceptions.tokens;

import jbst.foundation.domain.base.Username;

public class RefreshTokenDbNotFoundException extends Exception {

    public RefreshTokenDbNotFoundException(Username username) {
        super("JWT refresh token is not present in database. Username: " + username);
    }
}
