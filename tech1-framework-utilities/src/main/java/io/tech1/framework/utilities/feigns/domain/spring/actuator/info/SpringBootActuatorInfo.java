package io.tech1.framework.utilities.feigns.domain.spring.actuator.info;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.tech1.framework.domain.base.Version;
import io.tech1.framework.domain.maven.MavenDetails;
import io.tech1.framework.utilities.feigns.domain.spring.actuator.info.git.SpringBootActuatorInfoGit;

import java.util.ArrayList;

import static io.tech1.framework.domain.constants.StringConstants.DASH;
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

    public static SpringBootActuatorInfo dash() {
        return new SpringBootActuatorInfo(
                SpringBootActuatorInfoGit.dash(),
                null,
                DASH,
                MavenDetails.dash()
        );
    }

    public static SpringBootActuatorInfo offline() {
        return dash();
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
    public String getProfileOrDash() {
        if (nonNull(this.activeProfile)) {
            return this.activeProfile;
        } else if (!isEmpty(this.activeProfiles)) {
            return this.activeProfiles.get(0);
        } else {
            return DASH;
        }
    }

    @JsonIgnore
    public boolean isProfileDash() {
        return DASH.equals(this.getProfileOrDash());
    }

    @JsonIgnore
    public Version getMavenVersionOrDash() {
        if (nonNull(this.maven)) {
            return this.maven.version();
        } else {
            return Version.dash();
        }
    }
}
