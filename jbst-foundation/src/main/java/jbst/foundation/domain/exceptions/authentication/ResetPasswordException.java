package jbst.foundation.domain.exceptions.authentication;

public class ResetPasswordException extends Exception {

    public ResetPasswordException(String message) {
        super(message);
    }

    public static ResetPasswordException userNotFound() {
        return new ResetPasswordException("User not found");
    }

    public static ResetPasswordException emailMissing() {
        return new ResetPasswordException("User email is missing");
    }

    public static ResetPasswordException emailNotConfirmed() {
        return new ResetPasswordException("User email is not confirmed");
    }
}
