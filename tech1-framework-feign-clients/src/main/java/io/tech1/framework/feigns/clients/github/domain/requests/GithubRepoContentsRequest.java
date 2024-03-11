package io.tech1.framework.feigns.clients.github.domain.requests;

public record GithubRepoContentsRequest(
        String token,
        String owner,
        String repo,
        String content
) {
}
