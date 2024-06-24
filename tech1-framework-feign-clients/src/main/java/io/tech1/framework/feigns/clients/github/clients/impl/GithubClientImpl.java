package io.tech1.framework.feigns.clients.github.clients.impl;

import feign.RetryableException;
import io.tech1.framework.feigns.clients.github.clients.GithubClient;
import io.tech1.framework.feigns.clients.github.definitions.GithubDefinition;
import io.tech1.framework.feigns.clients.github.domain.requests.GithubRepoContentsRequest;
import io.tech1.framework.feigns.clients.github.domain.responses.GithubRepoContentsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.tech1.framework.foundation.domain.constants.LogsConstants.SERVER_OFFLINE;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GithubClientImpl implements GithubClient {

    // Definitions
    private final GithubDefinition githubDefinition;

    @Override
    public GithubRepoContentsResponse getContents(GithubRepoContentsRequest request) {
        try {
            return this.githubDefinition.getContents(
                    request.token(),
                    request.owner(),
                    request.repo(),
                    request.content()
            );
        } catch (RetryableException ex) {
            LOGGER.warn(SERVER_OFFLINE, "GitHub", ex.getMessage());
            throw new IllegalArgumentException(ex);
        }
    }
}
