package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.hardware.monitoring.HardwareName;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Map;

import static io.tech1.framework.domain.asserts.Asserts.assertTrueOrThrow;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class HardwareMonitoringConfigs extends AbstractPropertiesConfigs {
    @NonMandatoryProperty
    private Map<HardwareName, BigDecimal> thresholdsConfigs;

    // NOTE: test-purposes
    public static HardwareMonitoringConfigs of(
            Map<HardwareName, BigDecimal> thresholdsConfigs
    ) {
        var instance = new HardwareMonitoringConfigs();
        instance.thresholdsConfigs = thresholdsConfigs;
        return instance;
    }

    @Override
    public void assertProperties() {
        super.assertProperties();
        assertTrueOrThrow(
                this.thresholdsConfigs.size() == 5,
                "Attribute `hardwareMonitoringConfigs.thresholdsConfigs` must contains 5 HardwareName elements"
        );
    }
}
