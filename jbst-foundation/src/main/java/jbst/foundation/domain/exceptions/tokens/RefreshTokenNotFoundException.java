package jbst.foundation.domain.exceptions.tokens;

public class RefreshTokenNotFoundException extends Exception {

    public RefreshTokenNotFoundException() {
        super("JWT refresh token not found");
    }
}
