package io.tech1.framework.domain.hardware.memories;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

import static io.tech1.framework.domain.utilities.numbers.RoundingUtility.scale;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class CpuMemory {
    @JsonValue
    private final BigDecimal value;

    @JsonCreator
    public CpuMemory(
            BigDecimal value
    ) {
        this.value = scale(value, 2);
    }

    public static CpuMemory zeroUsage() {
        return new CpuMemory(
                BigDecimal.ZERO
        );
    }
}
