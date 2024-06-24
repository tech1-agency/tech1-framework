package io.tech1.framework.foundation.domain.states.classic;

public record ClassicStatePermissions(
        boolean disabled,
        boolean startPermitted,
        boolean restartPermitted,
        boolean pausePermitted,
        boolean stopPermitted
) {
}
