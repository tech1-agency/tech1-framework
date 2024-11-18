package jbst.foundation.domain.base;

public interface AbstractAuthority {
    String SUPERADMIN = "superadmin";
    String INVITATION_CODE_READ = "invitations:read";
    String INVITATION_CODE_WRITE = "invitations:write";
    String PROMETHEUS_READ = "prometheus:read";

    String getValue();
}
