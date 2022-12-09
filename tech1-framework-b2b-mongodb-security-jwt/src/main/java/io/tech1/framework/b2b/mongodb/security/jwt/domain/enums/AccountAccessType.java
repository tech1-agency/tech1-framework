package io.tech1.framework.b2b.mongodb.security.jwt.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum AccountAccessType {
    USERNAME_PASSWORD("username/password"),
    SECURITY_TOKEN("security token");

    @Getter
    private final String value;

    @Override
    public String toString() {
        return this.value;
    }
}
