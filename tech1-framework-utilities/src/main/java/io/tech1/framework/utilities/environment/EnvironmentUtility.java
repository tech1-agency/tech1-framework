package io.tech1.framework.utilities.environment;

public interface EnvironmentUtility {
    void verifyProfilesConfiguration();
    String getActiveProfile();
    boolean isDev();
    boolean isStage();
    boolean isProd();
    boolean isProfile(String profileName);
}
