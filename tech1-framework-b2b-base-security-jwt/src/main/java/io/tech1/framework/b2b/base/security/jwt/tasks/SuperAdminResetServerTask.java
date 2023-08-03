package io.tech1.framework.b2b.base.security.jwt.tasks;

import io.tech1.framework.b2b.base.security.jwt.domain.jwt.JwtUser;
import io.tech1.framework.domain.system.reset_server.ResetServerStatus;

public interface SuperAdminResetServerTask {
    ResetServerStatus getStatus();
    void reset(JwtUser user);
}
