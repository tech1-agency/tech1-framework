package io.tech1.framework.domain.hardware.memories;

public record SystemMemories(
        GlobalMemory global,
        CpuMemory cpu
) {
}
