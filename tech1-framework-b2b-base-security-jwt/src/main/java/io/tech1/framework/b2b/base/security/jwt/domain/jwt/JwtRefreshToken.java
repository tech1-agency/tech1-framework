package io.tech1.framework.b2b.base.security.jwt.domain.jwt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.tech1.framework.domain.constants.StringConstants.UNKNOWN;
import static io.tech1.framework.domain.utilities.random.RandomUtility.randomString;

public record JwtRefreshToken(@NotNull String value) {

    @JsonCreator
    public static JwtRefreshToken of(String value) {
        return new JwtRefreshToken(value);
    }

    public static JwtRefreshToken random() {
        return new JwtRefreshToken(randomString());
    }

    public static JwtRefreshToken unknown() {
        return of(UNKNOWN);
    }

    public static JwtRefreshToken testsHardcoded() {
        return of("B7C50972C873270CD7B2");
    }

    public static Set<JwtRefreshToken> refreshTokens(String... tokens) {
        return Stream.of(tokens).map(JwtRefreshToken::new).collect(Collectors.toSet());
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
