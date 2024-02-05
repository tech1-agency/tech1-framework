package io.tech1.framework.domain.exceptions.tokens;

public class CsrfTokenNotFoundException extends Exception {

    public CsrfTokenNotFoundException() {
        super("Csrf token not found");
    }
}
