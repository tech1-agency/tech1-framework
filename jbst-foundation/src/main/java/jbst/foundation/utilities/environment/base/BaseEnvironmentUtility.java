package jbst.foundation.utilities.environment.base;

import jbst.foundation.domain.constants.StringConstants;
import jbst.foundation.utilities.environment.EnvironmentUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static jbst.foundation.domain.asserts.Asserts.assertTrueOrThrow;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseEnvironmentUtility implements EnvironmentUtility {

    private final Environment environment;

    @Override
    public void verifyOneActiveProfile() {
        assertTrueOrThrow(this.environment.getActiveProfiles().length == 1, "Expected one active profile");
    }

    @Override
    public String getOneActiveProfile() {
        return this.environment.getActiveProfiles()[0];
    }

    @Override
    public String getOneActiveProfileOrDash() {
        var activeProfiles = new ArrayList<>(List.of(this.environment.getActiveProfiles()));
        if (isEmpty(activeProfiles) || activeProfiles.size() > 1) {
            return StringConstants.DASH;
        } else {
            return activeProfiles.get(0);
        }
    }

    @Override
    public boolean isDev() {
        return this.isProfile("dev");
    }

    @Override
    public boolean isStage() {
        return this.isProfile("stage");
    }

    @Override
    public boolean isProd() {
        return this.isProfile("prod");
    }

    @Override
    public boolean isProfile(String profile) {
        return profile.equals(this.getOneActiveProfile());
    }
}
