package jbst.foundation.domain.concurrent;

public record TimerTaskPermissions(
        boolean start,
        boolean stop
) {
}
