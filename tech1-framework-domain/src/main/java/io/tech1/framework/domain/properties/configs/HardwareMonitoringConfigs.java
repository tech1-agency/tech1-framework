package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.hardware.monitoring.HardwareName;
import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.utilities.enums.EnumUtility;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.tech1.framework.domain.asserts.Asserts.assertTrueOrThrow;
import static io.tech1.framework.domain.utilities.enums.EnumUtility.baseJoining;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.missingMappingsKeys;
import static org.apache.commons.collections4.SetUtils.disjunction;

// Lombok (property-based)
@Data
@EqualsAndHashCode(callSuper = true)
public class HardwareMonitoringConfigs extends AbstractPropertiesToggleConfigs {
    @MandatoryProperty
    private boolean enabled;
    @MandatoryProperty
    private Map<HardwareName, BigDecimal> thresholdsConfigs;

    // NOTE: test-purposes
    public static HardwareMonitoringConfigs of(
            boolean enabled,
            Map<HardwareName, BigDecimal> thresholdsConfigs
    ) {
        var instance = new HardwareMonitoringConfigs();
        instance.enabled = enabled;
        instance.thresholdsConfigs = thresholdsConfigs;
        return instance;
    }

    @Override
    public void assertProperties() {
        super.assertProperties();
        if (this.enabled) {
            var disjunction = disjunction(this.thresholdsConfigs.keySet(), EnumUtility.set(HardwareName.class));
            assertTrueOrThrow(
                    this.thresholdsConfigs.size() == 5,
                    missingMappingsKeys(
                            "hardwareMonitoringConfigs.thresholdsConfigs",
                            baseJoining(HardwareName.class),
                            baseJoining(disjunction)
                    )
            );
        }
    }

    public Map<HardwareName, BigDecimal> getThresholdsConfigs() {
        if (this.enabled) {
            return this.thresholdsConfigs;
        } else {
            return Stream.of(HardwareName.values())
                    .collect(
                            Collectors.toMap(
                                    entry -> entry,
                                    entry -> BigDecimal.ZERO
                            )
                    );
        }
    }
}
