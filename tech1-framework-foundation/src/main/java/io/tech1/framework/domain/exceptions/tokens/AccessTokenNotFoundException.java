package io.tech1.framework.domain.exceptions.tokens;

public class AccessTokenNotFoundException extends Exception {

    public AccessTokenNotFoundException() {
        super("JWT access token not found");
    }
}
