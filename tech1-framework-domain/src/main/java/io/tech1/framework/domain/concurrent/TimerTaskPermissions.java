package io.tech1.framework.domain.concurrent;

import lombok.Data;

// Lombok
@Data(staticConstructor = "of")
public class TimerTaskPermissions {
    private final boolean start;
    private final boolean stop;
}
