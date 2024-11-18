package tech1.framework.iam.domain.postgres.projections;

import tech1.framework.foundation.domain.base.Username;

public interface PostgresDbUserProjection1 {
    String getUsername();

    default Username getAsUsername() {
        return Username.of(this.getUsername());
    }
}
