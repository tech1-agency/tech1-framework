package io.tech1.framework.foundation.domain.hardware.memories;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.tech1.framework.foundation.domain.constants.BytesConstants;
import io.tech1.framework.foundation.domain.hardware.bytes.ByteSize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static io.tech1.framework.foundation.utilities.random.RandomUtility.randomLongGreaterThanZeroByBounds;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class GlobalMemory {
    private final ByteSize available;
    private final ByteSize total;
    private final ByteSize swapUsed;
    private final ByteSize swapTotal;
    private final ByteSize virtualUsed;
    private final ByteSize virtualTotal;

    @JsonCreator
    public GlobalMemory(
            @JsonProperty("available") long available,
            @JsonProperty("total") long total,
            @JsonProperty("swapUsed") long swapUsed,
            @JsonProperty("swapTotal") long swapTotal,
            @JsonProperty("virtualUsed") long virtualUsed,
            @JsonProperty("virtualTotal") long virtualTotal
    ) {
        this.available = new ByteSize(available);
        this.total = new ByteSize(total);
        this.swapUsed = new ByteSize(swapUsed);
        this.swapTotal = new ByteSize(swapTotal);
        this.virtualUsed = new ByteSize(virtualUsed);
        this.virtualTotal = new ByteSize(virtualTotal);
    }

    public static GlobalMemory zeroUsage() {
        var zero = 0L;
        return new GlobalMemory(
                zero,
                zero,
                zero,
                zero,
                zero,
                zero
        );
    }

    public static GlobalMemory random() {
        return new GlobalMemory(
                randomLongGreaterThanZeroByBounds(10, 500) * BytesConstants.BYTES_IN_MEGABYTE,
                randomLongGreaterThanZeroByBounds(10, 500) * BytesConstants.BYTES_IN_MEGABYTE,
                randomLongGreaterThanZeroByBounds(10, 500) * BytesConstants.BYTES_IN_MEGABYTE,
                randomLongGreaterThanZeroByBounds(10, 500) * BytesConstants.BYTES_IN_MEGABYTE,
                randomLongGreaterThanZeroByBounds(10, 500) * BytesConstants.BYTES_IN_MEGABYTE,
                randomLongGreaterThanZeroByBounds(10, 500) * BytesConstants.BYTES_IN_MEGABYTE
        );
    }

    public static GlobalMemory testsHardcoded() {
        return new GlobalMemory(
                1073741824L,
                1973741824L,
                1073741824L,
                1773741824L,
                1073741824L,
                1673741824L
        );
    }
}
