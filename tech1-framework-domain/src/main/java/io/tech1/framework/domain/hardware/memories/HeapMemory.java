package io.tech1.framework.domain.hardware.memories;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.tech1.framework.domain.hardware.bytes.ByteSize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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
}
