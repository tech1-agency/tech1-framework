package io.tech1.framework.domain.http.requests;

import io.tech1.framework.domain.constants.StringConstants;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;

import static java.util.Objects.isNull;

// Lombok
@Getter
@EqualsAndHashCode
@ToString
public class UserAgentHeader {
    private final String value;

    public UserAgentHeader(HttpServletRequest request) {
        if (isNull(request) || isNull(request.getHeader("User-Agent"))) {
            this.value = StringConstants.EMPTY;
        } else {
            this.value = request.getHeader("User-Agent");
        }
    }
}
