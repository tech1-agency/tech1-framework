package jbst.foundation.feigns.github.domain.requests;

public record GithubRepoContentsRequest(
        String token,
        String owner,
        String repo,
        String content
) {
}
