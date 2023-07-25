package io.tech1.framework.emails.domain;

import java.util.Map;
import java.util.Set;

public record EmailHTML(
        Set<String> to,
        String subject,
        String templateName,
        Map<String, Object> templateVariables
) {
}
