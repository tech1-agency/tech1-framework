package jbst.foundation.domain.base;

public interface AbstractAuthority {
    String SUPERADMIN = "superadmin";
    String INVITATION_CODE_READ = "invitationCode:read";
    String INVITATION_CODE_WRITE = "invitationCode:write";
    String PROMETHEUS_READ = "prometheus:read";

    String getValue();
}
