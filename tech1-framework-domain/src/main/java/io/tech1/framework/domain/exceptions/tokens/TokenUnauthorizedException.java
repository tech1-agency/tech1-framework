package io.tech1.framework.domain.exceptions.tokens;

public class TokenUnauthorizedException extends Exception {

    public TokenUnauthorizedException(String message) {
        super(message);
    }
}
