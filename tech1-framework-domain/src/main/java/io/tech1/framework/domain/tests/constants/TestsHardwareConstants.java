package io.tech1.framework.domain.tests.constants;

import io.tech1.framework.domain.hardware.memories.CpuMemory;
import io.tech1.framework.domain.hardware.memories.GlobalMemory;
import io.tech1.framework.domain.hardware.memories.HeapMemory;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class TestsHardwareConstants {
    public static final GlobalMemory GLOBAL_MEMORY = new GlobalMemory(
            1073741824L,
            1973741824L,
            1073741824L,
            1773741824L,
            1073741824L,
            1673741824L
    );
    public static final CpuMemory CPU_MEMORY = new CpuMemory(new BigDecimal("1.234"));
    public static final HeapMemory HEAP_MEMORY = new HeapMemory(1073741824L, 573741824L, 1073741824L, 1073741824L);
}
