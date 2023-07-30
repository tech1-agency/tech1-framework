package io.tech1.framework.b2b.base.security.jwt.repositories;

import io.tech1.framework.domain.base.Username;

public interface AnyDbInvitationCodesRepository {
    long countByOwner(Username username);
}
