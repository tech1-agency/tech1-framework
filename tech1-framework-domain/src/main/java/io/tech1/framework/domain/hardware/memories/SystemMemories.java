package io.tech1.framework.domain.hardware.memories;

public record SystemMemories(
        GlobalMemory global,
        CpuMemory cpu
) {

    public static SystemMemories testsHardcoded() {
        return new SystemMemories(
                GlobalMemory.testsHardcoded(),
                CpuMemory.testsHardcoded()
        );
    }
}
