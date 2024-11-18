package jbst.foundation.domain.hardware.memories;

public record SystemMemories(
        GlobalMemory global,
        CpuMemory cpu
) {

    public static SystemMemories hardcoded() {
        return new SystemMemories(
                GlobalMemory.hardcoded(),
                CpuMemory.hardcoded()
        );
    }
}
