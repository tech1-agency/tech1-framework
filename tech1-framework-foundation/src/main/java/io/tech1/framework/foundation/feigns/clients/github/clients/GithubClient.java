package io.tech1.framework.foundation.feigns.clients.github.clients;

import io.tech1.framework.foundation.feigns.clients.github.domain.requests.GithubRepoContentsRequest;
import io.tech1.framework.foundation.feigns.clients.github.domain.responses.GithubRepoContentsResponse;

public interface GithubClient {
    GithubRepoContentsResponse getContents(GithubRepoContentsRequest request);
}
