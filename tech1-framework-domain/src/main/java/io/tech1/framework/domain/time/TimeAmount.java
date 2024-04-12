package io.tech1.framework.domain.time;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.temporal.ChronoUnit;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class TimeAmount {
    private final long amount;
    private final ChronoUnit unit;

    @JsonCreator
    public TimeAmount(
            @JsonProperty("amount") long amount,
            @JsonProperty("unit") ChronoUnit unit
    ) {
        assertNonNullOrThrow(unit, invalidAttribute("TimeAmount.unit"));
        this.amount = amount;
        this.unit = unit;
    }

    public static TimeAmount testsHardcoded() {
        return new TimeAmount(30L, ChronoUnit.SECONDS);
    }

    public static TimeAmount forever() {
        return new TimeAmount(1L, ChronoUnit.FOREVER);
    }

    public long toSeconds() {
        return this.amount * this.getUnit().getDuration().toSeconds();
    }

    public long toMillis() {
        return this.amount * this.getUnit().getDuration().toMillis();
    }
}
