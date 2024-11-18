package jbst.foundation.domain.exceptions.tokens;

public class AccessTokenInvalidException extends Exception {

    public AccessTokenInvalidException() {
        super("JWT access token is invalid");
    }
}
