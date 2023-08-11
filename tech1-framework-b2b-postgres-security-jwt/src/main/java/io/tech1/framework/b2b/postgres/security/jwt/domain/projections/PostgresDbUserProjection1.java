package io.tech1.framework.b2b.postgres.security.jwt.domain.projections;

import io.tech1.framework.domain.base.Username;

public interface PostgresDbUserProjection1 {
    String getUsername();

    default Username getAsUsername() {
        return Username.of(this.getUsername());
    }
}
