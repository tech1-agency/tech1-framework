package io.tech1.framework.domain.properties.configs;

import io.tech1.framework.domain.hardware.monitoring.HardwareName;
import io.tech1.framework.domain.properties.annotations.MandatoryProperty;
import io.tech1.framework.domain.properties.annotations.NonMandatoryProperty;
import io.tech1.framework.domain.utilities.enums.EnumUtility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.tech1.framework.domain.asserts.Asserts.assertTrueOrThrow;
import static io.tech1.framework.domain.utilities.enums.EnumUtility.baseJoining;
import static io.tech1.framework.domain.utilities.exceptions.ExceptionsMessagesUtility.missingMappingsKeys;
import static org.apache.commons.collections4.SetUtils.disjunction;

// Lombok (property-based)
@AllArgsConstructor(onConstructor = @__({@ConstructorBinding}))
@Data
@EqualsAndHashCode(callSuper = true)
public class HardwareMonitoringConfigs extends AbstractPropertiesToggleConfigs {
    @MandatoryProperty
    private final boolean enabled;
    @NonMandatoryProperty
    private Map<HardwareName, BigDecimal> thresholdsConfigs;

    public static HardwareMonitoringConfigs testsHardcoded() {
        return new HardwareMonitoringConfigs(
                true,
                new EnumMap<>(
                        Map.of(
                                HardwareName.CPU, new BigDecimal("80"),
                                HardwareName.HEAP, new BigDecimal("85"),
                                HardwareName.SERVER, new BigDecimal("90"),
                                HardwareName.SWAP, new BigDecimal("95"),
                                HardwareName.VIRTUAL, new BigDecimal("98")
                        )
                )
        );
    }

    public static HardwareMonitoringConfigs disabled() {
        return new HardwareMonitoringConfigs(
                false,
                new EnumMap<>(HardwareName.class)
        );
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
