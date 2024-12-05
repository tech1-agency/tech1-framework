package jbst.foundation.domain.exceptions.tokens;

public class UserTokenValidationException extends Exception {

    public UserTokenValidationException(String message) {
        super(message);
    }

    public static UserTokenValidationException notFound() {
        return new UserTokenValidationException("Token not found");
    }

    public static UserTokenValidationException used() {
        return new UserTokenValidationException("Token is used");
    }

    public static UserTokenValidationException expired() {
        return new UserTokenValidationException("Token is expired");
    }

    public static UserTokenValidationException invalidType() {
        return new UserTokenValidationException("Token type is invalid");
    }
}
