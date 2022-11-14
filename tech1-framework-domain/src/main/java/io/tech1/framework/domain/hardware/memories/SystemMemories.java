package io.tech1.framework.domain.hardware.memories;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

// Lombok
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class SystemMemories {
    private final GlobalMemory global;
    private final CpuMemory cpu;
}
