package tech1.framework.iam.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum AccountAccessMethod {
    USERNAME_PASSWORD("username/password"),
    SECURITY_TOKEN("security token");

    @Getter
    private final String value;
}
