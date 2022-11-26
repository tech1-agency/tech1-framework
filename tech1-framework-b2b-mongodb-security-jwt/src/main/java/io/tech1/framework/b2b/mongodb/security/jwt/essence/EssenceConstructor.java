package io.tech1.framework.b2b.mongodb.security.jwt.essence;

public interface EssenceConstructor {
    boolean isDefaultUsersEnabled();
    boolean isInvitationCodesEnabled();
    void addDefaultUsers();
    void addDefaultUsersInvitationCodes();
}
