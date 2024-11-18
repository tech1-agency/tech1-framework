package jbst.iam.domain.jwt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static tech1.framework.foundation.domain.constants.StringConstants.UNKNOWN;
import static tech1.framework.foundation.utilities.random.RandomUtility.randomString;

public record JwtAccessToken(@NotNull String value) {

    @JsonCreator
    public static JwtAccessToken of(String value) {
        return new JwtAccessToken(value);
    }

    public static JwtAccessToken random() {
        return new JwtAccessToken(randomString());
    }

    public static JwtAccessToken unknown() {
        return of(UNKNOWN);
    }

    public static JwtAccessToken testsHardcoded() {
        return of("D9F4AF096BEE11C93D84");
    }

    public static Set<JwtAccessToken> accessTokens(String... tokens) {
        return Stream.of(tokens).map(JwtAccessToken::new).collect(Collectors.toSet());
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
