package io.tech1.framework.domain.concurrent;

import lombok.Data;

// Lombok
@Data
public class TimerTaskPermissions {
    private final boolean start;
    private final boolean stop;
}
