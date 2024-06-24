package io.tech1.framework.domain.hardware.monitoring;

import io.tech1.framework.domain.hardware.bytes.ByteSize;
import io.tech1.framework.domain.hardware.memories.GlobalMemory;
import io.tech1.framework.domain.hardware.memories.HeapMemory;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.foundation.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class HardwareMonitoringMaxValues {
    private final ByteSize server;
    private final ByteSize swap;
    private final ByteSize virtual;
    private final ByteSize heap;

    public HardwareMonitoringMaxValues(
            GlobalMemory global,
            HeapMemory heap
    ) {
        assertNonNullOrThrow(global, invalidAttribute("HardwareMonitoringMaxValues.global"));
        assertNonNullOrThrow(heap, invalidAttribute("HardwareMonitoringMaxValues.heap"));
        this.server = global.getTotal();
        this.swap = global.getSwapTotal();
        this.virtual = global.getVirtualTotal();
        this.heap = heap.getMax();
    }

    public static HardwareMonitoringMaxValues random() {
        return new HardwareMonitoringMaxValues(
                GlobalMemory.random(),
                HeapMemory.random()
        );
    }
}
