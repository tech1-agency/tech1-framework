package tech1.framework.foundation.utilities.environment;

public interface EnvironmentUtility {
    void verifyOneActiveProfile();
    String getOneActiveProfile();
    String getOneActiveProfileOrDash();
    boolean isDev();
    boolean isStage();
    boolean isProd();
    boolean isProfile(String profileName);
}
