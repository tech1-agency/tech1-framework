package jbst.foundation.domain.http.cache;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;

import static jbst.foundation.utilities.random.RandomUtility.randomString;

public record CachedPayload(@NotNull String value) {

    @JsonCreator
    public static CachedPayload of(String value) {
        return new CachedPayload(value);
    }

    public static CachedPayload hardcoded() {
        return of("{}");
    }

    public static CachedPayload random() {
        return of(randomString());
    }

    @JsonValue
    @Override
    public String toString() {
        return this.value;
    }
}
