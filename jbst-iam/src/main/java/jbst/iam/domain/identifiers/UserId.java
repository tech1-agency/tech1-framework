package jbst.iam.domain.identifiers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import static tech1.framework.foundation.domain.constants.StringConstants.UNKNOWN;
import static tech1.framework.foundation.utilities.random.RandomUtility.randomString;

public record UserId(@NotNull String value) {

    @JsonCreator
    public static UserId of(String value) {
        return new UserId(value);
    }

    public static UserId random() {
        return new UserId(randomString());
    }

    public static UserId unknown() {
        return of(UNKNOWN);
    }

    public static UserId testsHardcoded() {
        return of("72667893848372913475");
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
