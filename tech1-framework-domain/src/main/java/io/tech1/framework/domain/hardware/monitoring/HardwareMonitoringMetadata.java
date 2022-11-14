package io.tech1.framework.domain.hardware.monitoring;

import io.tech1.framework.domain.base.Version;
import io.tech1.framework.domain.hardware.memories.SystemMemories;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

// Lombok
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class HardwareMonitoringMetadata {
    private final Version platformVersion;
    private final SystemMemories systemMemories;
}
