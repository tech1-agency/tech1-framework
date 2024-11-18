package jbst.foundation.domain.base;

public interface AbstractAuthority {
    String SUPERADMIN = "superadmin";
    String INVITATIONS_READ = "invitations:read";
    String INVITATIONS_WRITE = "invitations:write";
    @SuppressWarnings("unused")
    String PROMETHEUS_READ = "prometheus:read";

    String getValue();
}
