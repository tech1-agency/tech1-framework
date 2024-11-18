package tech1.framework.foundation.domain.exceptions.tokens;

public class AccessTokenNotFoundException extends Exception {

    public AccessTokenNotFoundException() {
        super("JWT access token not found");
    }
}
