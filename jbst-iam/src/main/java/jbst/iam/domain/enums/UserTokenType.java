package jbst.iam.domain.enums;

public enum UserTokenType {
    EMAIL_CONFIRMATION,
    PASSWORD_RESET;

    public boolean isEmailConfirmation() {
        return EMAIL_CONFIRMATION.equals(this);
    }

    public boolean isPasswordReset() {
        return PASSWORD_RESET.equals(this);
    }
}
