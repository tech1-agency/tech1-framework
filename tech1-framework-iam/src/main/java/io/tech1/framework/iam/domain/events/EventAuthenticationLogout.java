package io.tech1.framework.iam.domain.events;

import io.tech1.framework.foundation.domain.base.Username;

public record EventAuthenticationLogout(
        Username username
) {
}
