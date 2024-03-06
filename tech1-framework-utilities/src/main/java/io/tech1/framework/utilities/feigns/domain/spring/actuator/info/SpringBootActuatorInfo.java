package io.tech1.framework.utilities.feigns.domain.spring.actuator.info;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.tech1.framework.domain.base.Version;
import io.tech1.framework.domain.maven.MavenDetails;
import io.tech1.framework.utilities.feigns.domain.spring.actuator.info.git.SpringBootActuatorInfoGit;

import java.util.ArrayList;
import java.util.Objects;

import static io.tech1.framework.domain.constants.StringConstants.UNDEFINED;
import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @param git            spring-based
 * @param activeProfiles spring-framework: String[]
 * @param activeProfile  tech1-framework: BaseInfoResource
 * @param maven  tech1-framework: BaseInfoResource
 */
public record SpringBootActuatorInfo(
        @JsonInclude(JsonInclude.Include.NON_NULL) SpringBootActuatorInfoGit git,
        @JsonInclude(JsonInclude.Include.NON_NULL) ArrayList<String> activeProfiles,
        @JsonInclude(JsonInclude.Include.NON_NULL) String activeProfile,
        @JsonInclude(JsonInclude.Include.NON_NULL) MavenDetails maven
) {
    public static SpringBootActuatorInfo undefined() {
        return new SpringBootActuatorInfo(
                SpringBootActuatorInfoGit.undefined(),
                null,
                UNDEFINED,
                MavenDetails.undefined()
        );
    }

    public static SpringBootActuatorInfo testsHardcoded() {
        return new SpringBootActuatorInfo(
                SpringBootActuatorInfoGit.testsHardcoded(),
                null,
                "dev",
                MavenDetails.testsHardcoded()
        );
    }

    @JsonIgnore
    public String getProfile() {
        if (nonNull(this.activeProfile)) {
            return this.activeProfile;
        } else if (!isEmpty(this.activeProfiles)) {
            return this.activeProfiles.get(0);
        } else {
            return UNDEFINED;
        }
    }

    @JsonIgnore
    public boolean isProfileUndefined() {
        return UNDEFINED.equals(this.getProfile());
    }

    @JsonIgnore
    public Version getMavenVersion() {
        if (Objects.nonNull(this.maven)) {
            return this.maven.version();
        } else {
            return Version.undefined();
        }
    }
}
