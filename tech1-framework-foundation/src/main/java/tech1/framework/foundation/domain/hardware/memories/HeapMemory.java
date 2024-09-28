package tech1.framework.foundation.domain.hardware.memories;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import tech1.framework.foundation.domain.constants.BytesConstants;
import tech1.framework.foundation.domain.hardware.bytes.ByteSize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static tech1.framework.foundation.utilities.random.RandomUtility.randomLongGreaterThanZeroByBounds;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class HeapMemory {
    private final ByteSize initial;
    private final ByteSize used;
    private final ByteSize max;
    private final ByteSize committed;

    @JsonCreator
    public HeapMemory(
            @JsonProperty("initial") long initial,
            @JsonProperty("used") long used,
            @JsonProperty("max") long max,
            @JsonProperty("committed") long committed
    ) {
        this.initial = new ByteSize(initial);
        this.used = new ByteSize(used);
        this.max = new ByteSize(max);
        this.committed = new ByteSize(committed);
    }

    public static HeapMemory zeroUsage() {
        var zero = 0L;
        return new HeapMemory(
                zero,
                zero,
                zero,
                zero
        );
    }

    public static HeapMemory random() {
        return new HeapMemory(
                randomLongGreaterThanZeroByBounds(10, 500) * BytesConstants.BYTES_IN_MEGABYTE,
                randomLongGreaterThanZeroByBounds(10, 500) * BytesConstants.BYTES_IN_MEGABYTE,
                randomLongGreaterThanZeroByBounds(10, 500) * BytesConstants.BYTES_IN_MEGABYTE,
                randomLongGreaterThanZeroByBounds(10, 500) * BytesConstants.BYTES_IN_MEGABYTE
        );
    }

    public static HeapMemory testsHardcoded() {
        return new HeapMemory(
                1073741824L,
                573741824L,
                1073741824L,
                1073741824L
        );
    }
}
