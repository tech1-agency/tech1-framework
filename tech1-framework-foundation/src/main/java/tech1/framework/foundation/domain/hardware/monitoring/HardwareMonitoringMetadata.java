package tech1.framework.foundation.domain.hardware.monitoring;

import tech1.framework.foundation.domain.base.Version;
import tech1.framework.foundation.domain.hardware.memories.SystemMemories;

public record HardwareMonitoringMetadata(
        Version version,
        SystemMemories systemMemories
) {
}
