package io.tech1.framework.utilities.feigns.domain.spring.actuator.info;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.tech1.framework.utilities.feigns.domain.spring.actuator.info.git.SpringBootActuatorInfoGit;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;

import static io.tech1.framework.domain.constants.StringConstants.UNDEFINED;
import static io.tech1.framework.utilities.feigns.domain.spring.actuator.info.git.SpringBootActuatorInfoGit.undefinedSpringBootActuatorInfoGit;
import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

// Lombok
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class SpringBootActuatorInfo {
    // spring-based
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final SpringBootActuatorInfoGit git;
    // spring-framework: String[]
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final ArrayList<String> activeProfiles;
    // tech1-framework: BaseInfoResource
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String activeProfile;

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
