package io.tech1.framework.domain.exceptions.tokens;

public class CookieUnauthorizedException extends Exception {

    public CookieUnauthorizedException(String message) {
        super(message);
    }
}
