package io.tech1.framework.domain.tests.constants;

import io.tech1.framework.domain.hardware.monitoring.HardwareName;
import io.tech1.framework.domain.properties.base.RemoteServer;
import io.tech1.framework.domain.properties.configs.*;
import io.tech1.framework.domain.properties.configs.incidents.IncidentFeatureConfigs;
import io.tech1.framework.domain.properties.configs.incidents.IncidentFeaturesConfigs;
import io.tech1.framework.domain.properties.configs.mvc.CorsConfigs;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.Map;

import static io.tech1.framework.domain.utilities.random.RandomUtility.randomEmail;

@UtilityClass
public class PropertiesConstants {
    public static final AsyncConfigs ASYNC_CONFIGS = AsyncConfigs.of("tech1-async");
    public static final EventsConfigs EVENTS_CONFIGS = EventsConfigs.of("tech1-events");
    public static final MvcConfigs MVC_CONFIGS = MvcConfigs.of(
            true,
            "/platform/security",
            CorsConfigs.of(
                    true,
                    "/api/**",
                    new String[] { "http://localhost:8080", "http://localhost:8081" },
                    new String[] { "GET", "POST" },
                    new String[] { "Access-Control-Allow-Origin" },
                    true,
                    null
            )
    );
    public static final EmailConfigs EMAIL_CONFIGS = EmailConfigs.of(
            false,
            "smtp.gmail.com",
            "587",
            "tech1@gmail.com",
            "Tech1",
            "Password123!",
            new String[] { randomEmail(), randomEmail() }
    );
    public static final IncidentConfigs INCIDENT_CONFIGS = IncidentConfigs.of(
            true,
            RemoteServer.of(
                    "http://localhost:8973",
                    "incident-user",
                    "incident-password"
            ),
            IncidentFeaturesConfigs.of(
                    IncidentFeatureConfigs.enabledIncidentFeatureConfigs(),
                    IncidentFeatureConfigs.disabledIncidentFeatureConfigs(),
                    IncidentFeatureConfigs.enabledIncidentFeatureConfigs(),
                    IncidentFeatureConfigs.disabledIncidentFeatureConfigs(),
                    IncidentFeatureConfigs.enabledIncidentFeatureConfigs(),
                    IncidentFeatureConfigs.disabledIncidentFeatureConfigs(),
                    IncidentFeatureConfigs.enabledIncidentFeatureConfigs(),
                    IncidentFeatureConfigs.disabledIncidentFeatureConfigs()
            )
    );
    public static final HardwareMonitoringConfigs HARDWARE_MONITORING_CONFIGS = HardwareMonitoringConfigs.of(
            Map.of(
                    HardwareName.CPU, new BigDecimal("80"),
                    HardwareName.HEAP, new BigDecimal("85"),
                    HardwareName.SERVER, new BigDecimal("90"),
                    HardwareName.SWAP, new BigDecimal("95"),
                    HardwareName.VIRTUAL, new BigDecimal("98")
            )
    );
    public static final HardwareServerConfigs HARDWARE_SERVER_CONFIGS = HardwareServerConfigs.of(
            "http://localhost:8484"
    );
}
