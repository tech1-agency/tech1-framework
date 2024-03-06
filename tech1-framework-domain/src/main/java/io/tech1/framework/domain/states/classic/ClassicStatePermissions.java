package io.tech1.framework.domain.states.classic;

public record ClassicStatePermissions(
        boolean disabled,
        boolean startPermitted,
        boolean restartPermitted,
        boolean pausePermitted,
        boolean stopPermitted
) {
}
