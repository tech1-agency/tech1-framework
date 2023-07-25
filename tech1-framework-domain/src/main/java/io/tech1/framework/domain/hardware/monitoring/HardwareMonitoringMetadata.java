package io.tech1.framework.domain.hardware.monitoring;

import io.tech1.framework.domain.base.Version;
import io.tech1.framework.domain.hardware.memories.SystemMemories;

public record HardwareMonitoringMetadata(
        Version version,
        SystemMemories systemMemories
) {
}
