package io.tech1.framework.domain.concurrent;

public record TimerTaskPermissions(
        boolean start,
        boolean stop
) {
}
