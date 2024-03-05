package io.tech1.framework.domain.maven;

import io.tech1.framework.domain.base.Version;

public record MavenDetails(
        String groupId,
        String artifactId,
        Version version
) {
}
