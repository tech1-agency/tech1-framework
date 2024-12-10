package jbst.foundation.domain.exceptions.authentication;

public class JbstPasswordResetException extends Exception {

    public JbstPasswordResetException(String message) {
        super(message);
    }

    public static JbstPasswordResetException userNotFound() {
        return new JbstPasswordResetException("User not found");
    }

    public static JbstPasswordResetException emailMissing() {
        return new JbstPasswordResetException("User email is missing");
    }

    public static JbstPasswordResetException emailNotConfirmed() {
        return new JbstPasswordResetException("User email is not confirmed");
    }
}
