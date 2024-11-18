package jbst.foundation.feigns.clients.github.clients;

import jbst.foundation.feigns.clients.github.domain.requests.GithubRepoContentsRequest;
import jbst.foundation.feigns.clients.github.domain.responses.GithubRepoContentsResponse;

public interface GithubClient {
    GithubRepoContentsResponse getContents(GithubRepoContentsRequest request);
}
