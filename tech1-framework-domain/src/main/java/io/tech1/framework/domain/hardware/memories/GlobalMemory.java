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
}
