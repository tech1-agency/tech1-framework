package jbst.foundation.domain.exceptions.tokens;

import jbst.foundation.domain.base.Username;

public class AccessTokenDbNotFoundException extends Exception {

    public AccessTokenDbNotFoundException(Username username) {
        super("JWT access token is not present in database. Username: " + username);
    }
}
