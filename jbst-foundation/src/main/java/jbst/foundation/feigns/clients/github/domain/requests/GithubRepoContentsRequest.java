package jbst.foundation.feigns.clients.github.domain.requests;

public record GithubRepoContentsRequest(
        String token,
        String owner,
        String repo,
        String content
) {
}
