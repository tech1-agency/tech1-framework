package io.tech1.framework.emails.domain;

import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data(staticConstructor = "of")
public class EmailHTML {
    private final Set<String> to;
    private final String subject;
    private final String templateName;
    private final Map<String, Object> templateVariables;
}
