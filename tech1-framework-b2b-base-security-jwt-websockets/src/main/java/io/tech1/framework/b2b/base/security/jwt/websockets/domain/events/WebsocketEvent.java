package io.tech1.framework.b2b.base.security.jwt.websockets.domain.events;

import io.tech1.framework.domain.hardware.monitoring.HardwareMonitoringDatapointTableView;
import io.tech1.framework.domain.system.reset_server.ResetServerStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

import static io.tech1.framework.domain.asserts.Asserts.assertNonNullOrThrow;
import static io.tech1.framework.foundation.utilities.exceptions.ExceptionsMessagesUtility.invalidAttribute;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class WebsocketEvent {
    private final Map<String, Object> attributes;

    public WebsocketEvent() {
        this.attributes = new HashMap<>();
    }

    public WebsocketEvent(Map<String, Object> attributes) {
        this.attributes = new HashMap<>(attributes);
    }

    public static WebsocketEvent hardwareMonitoring(
            HardwareMonitoringDatapointTableView datapoint
    ) {
        assertNonNullOrThrow(datapoint, invalidAttribute("WebsocketEvent.hardwareMonitoring.datapoint"));
        return new WebsocketEvent(
                Map.of(
                        WebsocketEventsAttributes.Keys.TYPE, WebsocketEventsAttributes.Values.TYPE_HARDWARE_MONITORING,
                        "datapoint", datapoint
                )
        );
    }

    public static WebsocketEvent resetServerProgress(
            ResetServerStatus status
    ) {
        assertNonNullOrThrow(status, invalidAttribute("WebsocketEvent.resetServerProgress.status"));
        return new WebsocketEvent(
                Map.of(
                        WebsocketEventsAttributes.Keys.TYPE, WebsocketEventsAttributes.Values.TYPE_RESET_SERVER_PROGRESS,
                        "status", status
                )
        );
    }

    public void add(String key, Object value) {
        this.attributes.put(key, value);
    }
}
