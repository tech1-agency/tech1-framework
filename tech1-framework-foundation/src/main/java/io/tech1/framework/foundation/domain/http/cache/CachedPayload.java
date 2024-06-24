package io.tech1.framework.foundation.domain.http.cache;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import static io.tech1.framework.foundation.utilities.random.RandomUtility.randomString;

public record CachedPayload(@NotNull String value) {

    @JsonCreator
    public static CachedPayload of(String value) {
        return new CachedPayload(value);
    }

    public static CachedPayload random() {
        return of(randomString());
    }

    public static CachedPayload testsHardcoded() {
        return of("{}");
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
