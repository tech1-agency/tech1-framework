package jbst.foundation.domain.exceptions.tokens;

public class UserEmailConfirmException extends Exception {

    public UserEmailConfirmException(String message) {
        super(message);
    }

    public static UserEmailConfirmException tokenNotFound() {
        return new UserEmailConfirmException("Token not found");
    }

    public static UserEmailConfirmException userNotFound() {
        return new UserEmailConfirmException("User not found");
    }

}
