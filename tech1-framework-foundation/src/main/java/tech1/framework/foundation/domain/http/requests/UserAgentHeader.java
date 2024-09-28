package tech1.framework.foundation.domain.http.requests;

import tech1.framework.foundation.domain.constants.StringConstants;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import jakarta.servlet.http.HttpServletRequest;

import static java.util.Objects.isNull;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class UserAgentHeader {
    private final String value;

    public static UserAgentHeader testsHardcoded() {
        return new UserAgentHeader("Chrome, macOS on Desktop");
    }

    public UserAgentHeader(HttpServletRequest request) {
        if (isNull(request) || isNull(request.getHeader("User-Agent"))) {
            this.value = StringConstants.EMPTY;
        } else {
            this.value = request.getHeader("User-Agent");
        }
    }

    // =================================================================================================================
    // PRIVATE METHODS
    // =================================================================================================================
    private UserAgentHeader(String value) {
        this.value = value;
    }
}
