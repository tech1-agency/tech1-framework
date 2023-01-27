package io.tech1.framework.b2b.mongodb.security.jwt.services;

import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseInvitationCode1;
import io.tech1.framework.b2b.mongodb.security.jwt.domain.dto.responses.ResponseServerSessionsTable;

import java.util.List;

// todo [yy] naming -> BasedSuperAdminService
public interface SuperAdminService {
    List<ResponseInvitationCode1> findUnused();
    ResponseServerSessionsTable getServerSessions();
}
