package jbst.foundation.feigns.spring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jbst.foundation.domain.base.Version;
import jbst.foundation.domain.constants.JbstConstants;
import jbst.foundation.domain.maven.MavenDetails;

import java.util.ArrayList;

import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @param git               spring-based
 * @param activeProfiles    spring-framework: String[]
 * @param activeProfile     jbst: BaseInfoResource
 * @param maven             jbst: BaseInfoResource
 */
public record SpringBootActuatorInfo(
        @JsonInclude(JsonInclude.Include.NON_NULL) SpringBootActuatorInfoGit git,
        @JsonInclude(JsonInclude.Include.NON_NULL) ArrayList<String> activeProfiles,
        @JsonInclude(JsonInclude.Include.NON_NULL) String activeProfile,
        @JsonInclude(JsonInclude.Include.NON_NULL) MavenDetails maven
) {

    public static SpringBootActuatorInfo hardcoded() {
        return new SpringBootActuatorInfo(
                SpringBootActuatorInfoGit.hardcoded(),
                null,
                "dev",
                MavenDetails.hardcoded()
        );
    }

    public static SpringBootActuatorInfo dash() {
        return new SpringBootActuatorInfo(
                SpringBootActuatorInfoGit.dash(),
                null,
                JbstConstants.Symbols.DASH,
                MavenDetails.dash()
        );
    }

    public static SpringBootActuatorInfo offline() {
        return dash();
    }

    @JsonIgnore
    public String getProfileOrDash() {
        if (nonNull(this.activeProfile)) {
            return this.activeProfile;
        } else if (!isEmpty(this.activeProfiles)) {
            return this.activeProfiles.get(0);
        } else {
            return JbstConstants.Symbols.DASH;
        }
    }

    @JsonIgnore
    public boolean isProfileDash() {
        return JbstConstants.Symbols.DASH.equals(this.getProfileOrDash());
    }

    @JsonIgnore
    public Version getMavenVersionOrDash() {
        if (nonNull(this.maven)) {
            return this.maven.version();
        } else {
            return Version.dash();
        }
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public SpringBootActuatorInfoGit getGitOrDash() {
        if (nonNull(this.git)) {
            return this.git;
        } else {
            return SpringBootActuatorInfoGit.dash();
        }
    }

    public record SpringBootActuatorInfoCommit(
            @JsonProperty("id") String id,
            @JsonProperty("time") String time
    ) {

        public static SpringBootActuatorInfoCommit hardcoded() {
            return new SpringBootActuatorInfoCommit(
                    "1234567",
                    "01.01.2024 15:00:00"
            );
        }

        public static SpringBootActuatorInfoCommit dash() {
            return new SpringBootActuatorInfoCommit(
                    JbstConstants.Symbols.DASH,
                    JbstConstants.Symbols.DASH
            );
        }
    }

    public record SpringBootActuatorInfoGit(
            @JsonProperty("commit") SpringBootActuatorInfoCommit commit,
            @JsonProperty("branch") String branch
    ) {

        public static SpringBootActuatorInfoGit hardcoded() {
            return new SpringBootActuatorInfoGit(
                    SpringBootActuatorInfoCommit.hardcoded(),
                    "dev"
            );
        }

        public static SpringBootActuatorInfoGit dash() {
            return new SpringBootActuatorInfoGit(
                    SpringBootActuatorInfoCommit.dash(),
                    JbstConstants.Symbols.DASH
            );
        }
    }
}
