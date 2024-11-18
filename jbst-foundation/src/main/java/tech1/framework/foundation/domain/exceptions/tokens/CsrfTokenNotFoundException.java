package tech1.framework.foundation.domain.exceptions.tokens;

public class CsrfTokenNotFoundException extends Exception {

    public CsrfTokenNotFoundException() {
        super("Csrf token not found");
    }
}
