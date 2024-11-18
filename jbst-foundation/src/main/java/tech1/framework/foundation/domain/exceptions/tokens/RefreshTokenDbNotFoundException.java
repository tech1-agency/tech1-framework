package tech1.framework.foundation.domain.exceptions.tokens;

import tech1.framework.foundation.domain.base.Username;

public class RefreshTokenDbNotFoundException extends Exception {

    public RefreshTokenDbNotFoundException(Username username) {
        super("JWT refresh token is not present in database. Username: " + username);
    }
}
