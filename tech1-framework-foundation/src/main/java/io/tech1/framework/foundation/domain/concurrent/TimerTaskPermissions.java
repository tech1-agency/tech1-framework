package io.tech1.framework.foundation.domain.concurrent;

public record TimerTaskPermissions(
        boolean start,
        boolean stop
) {
}
