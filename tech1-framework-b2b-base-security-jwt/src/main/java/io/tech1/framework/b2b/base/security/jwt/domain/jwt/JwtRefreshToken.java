package io.tech1.framework.b2b.base.security.jwt.domain.jwt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

public record JwtRefreshToken(@NotNull String value) {

    @JsonCreator
    public static JwtRefreshToken of(String value) {
        return new JwtRefreshToken(value);
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
