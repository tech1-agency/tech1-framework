package jbst.iam.domain.db;

import lombok.Data;

@Data
public class UserEmailDetails {
    private final boolean emailRequired;
    private final boolean emailConfirmed;

    private UserEmailDetails(
            boolean emailRequired,
            boolean emailConfirmed
    ) {
        this.emailRequired = emailRequired;
        this.emailConfirmed = emailConfirmed;
    }

    public static UserEmailDetails unnecessary() {
        return new UserEmailDetails(false, false);
    }

    public static UserEmailDetails required() {
        return new UserEmailDetails(true, false);
    }

    public static UserEmailDetails confirmed() {
        return new UserEmailDetails(true, true);
    }

    public boolean isEnabled() {
        if (this.emailRequired) {
            return this.emailConfirmed;
        } else {
            return true;
        }
    }

}
