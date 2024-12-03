package jbst.iam.domain.enums;

public enum UserEmailTokenType {
    VERIFICATION,
    RESET_PASSWORD;

    public boolean isVerification() {
        return VERIFICATION.equals(this);
    }

    public boolean isResetPassword() {
        return RESET_PASSWORD.equals(this);
    }
}
