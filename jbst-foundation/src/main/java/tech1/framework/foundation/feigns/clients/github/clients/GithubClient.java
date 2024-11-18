package tech1.framework.foundation.feigns.clients.github.clients;

import tech1.framework.foundation.feigns.clients.github.domain.requests.GithubRepoContentsRequest;
import tech1.framework.foundation.feigns.clients.github.domain.responses.GithubRepoContentsResponse;

public interface GithubClient {
    GithubRepoContentsResponse getContents(GithubRepoContentsRequest request);
}
