package jbst.foundation.services.emails.domain;

import jbst.foundation.domain.base.Email;

import java.util.Map;
import java.util.Set;

public record EmailHTML(
        Set<String> to,
        String subject,
        String templateName,
        Map<String, Object> templateVariables
) {

    public static EmailHTML of(
            Email to,
            String subject,
            String templateName,
            Map<String, Object> templateVariables
    ) {
        return new EmailHTML(Set.of(to.value()), subject, templateName, templateVariables);
    }

    public static EmailHTML hardcoded() {
        return new EmailHTML(Set.of(), "Account Accessed", "jbst-account-accessed", Map.of());
    }
}
