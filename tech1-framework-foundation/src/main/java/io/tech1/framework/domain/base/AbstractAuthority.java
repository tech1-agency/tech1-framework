package io.tech1.framework.domain.base;

public interface AbstractAuthority {
    String SUPERADMIN = "superadmin";
    String INVITATION_CODE_READ = "invitationCode:read";
    String INVITATION_CODE_WRITE = "invitationCode:write";

    String getValue();
}
