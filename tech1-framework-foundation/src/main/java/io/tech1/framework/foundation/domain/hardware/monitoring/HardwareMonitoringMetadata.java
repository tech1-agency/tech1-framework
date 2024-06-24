package io.tech1.framework.foundation.domain.hardware.monitoring;

import io.tech1.framework.foundation.domain.base.Version;
import io.tech1.framework.foundation.domain.hardware.memories.SystemMemories;

public record HardwareMonitoringMetadata(
        Version version,
        SystemMemories systemMemories
) {
}
