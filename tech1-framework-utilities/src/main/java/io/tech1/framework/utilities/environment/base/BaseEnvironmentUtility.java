package io.tech1.framework.utilities.environment.base;

import io.tech1.framework.utilities.environment.EnvironmentUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import static io.tech1.framework.domain.asserts.Asserts.assertTrueOrThrow;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseEnvironmentUtility implements EnvironmentUtility {

    private static final String PROFILE_DEVELOPMENT = "dev";
    private static final String PROFILE_STAGE = "stage";
    private static final String PROFILE_PRODUCTION = "prod";

    private final Environment environment;

    @Override
    public void verifyProfilesConfiguration() {
        assertTrueOrThrow(this.environment.getActiveProfiles().length == 1, "Base Environment Utility contains ONLY one active profile");
    }

    @Override
    public String getActiveProfile() {
        return this.environment.getActiveProfiles()[0];
    }

    @Override
    public boolean isDev() {
        return this.isProfile(PROFILE_DEVELOPMENT);
    }

    @Override
    public boolean isStage() {
        return this.isProfile(PROFILE_STAGE);
    }

    @Override
    public boolean isProd() {
        return this.isProfile(PROFILE_PRODUCTION);
    }

    @Override
    public boolean isProfile(String profile) {
        return profile.equals(this.getActiveProfile());
    }
}
