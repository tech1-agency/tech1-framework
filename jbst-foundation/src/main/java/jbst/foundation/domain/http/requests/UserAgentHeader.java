package jbst.foundation.domain.http.requests;

import jakarta.servlet.http.HttpServletRequest;
import jbst.foundation.domain.constants.StringConstants;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static java.util.Objects.isNull;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class UserAgentHeader {
    private final String value;

    public static UserAgentHeader hardcoded() {
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
