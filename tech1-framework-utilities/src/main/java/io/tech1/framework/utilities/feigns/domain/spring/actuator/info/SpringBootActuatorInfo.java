package io.tech1.framework.utilities.feigns.domain.spring.actuator.info;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.tech1.framework.utilities.feigns.domain.spring.actuator.info.git.SpringBootActuatorInfoGit;

import java.util.ArrayList;

import static io.tech1.framework.domain.constants.StringConstants.UNDEFINED;
import static io.tech1.framework.utilities.feigns.domain.spring.actuator.info.git.SpringBootActuatorInfoGit.undefinedSpringBootActuatorInfoGit;
import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @param git            spring-based
 * @param activeProfiles spring-framework: String[]
 * @param activeProfile  tech1-framework: BaseInfoResource
 */
public record SpringBootActuatorInfo(
        @JsonInclude(JsonInclude.Include.NON_NULL) SpringBootActuatorInfoGit git,
        @JsonInclude(JsonInclude.Include.NON_NULL) ArrayList<String> activeProfiles,
        @JsonInclude(JsonInclude.Include.NON_NULL) String activeProfile
) {
    public static SpringBootActuatorInfo undefinedSpringBootActuatorInfo() {
        return new SpringBootActuatorInfo(
                undefinedSpringBootActuatorInfoGit(),
                null,
                UNDEFINED
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
    public boolean isUndefined() {
        return UNDEFINED.equals(this.getProfile());
    }
}
