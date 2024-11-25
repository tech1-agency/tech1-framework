package jbst.foundation.feigns.github;

import feign.RetryableException;
import jbst.foundation.domain.constants.JbstConstants;
import jbst.foundation.feigns.github.domain.requests.GithubRepoContentsRequest;
import jbst.foundation.feigns.github.domain.responses.GithubRepoContentsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GithubClient {

    // Definitions
    private final GithubDefinition definition;

    public final GithubRepoContentsResponse getContents(GithubRepoContentsRequest request) {
        try {
            return this.definition.getContents(
                    request.token(),
                    request.owner(),
                    request.repo(),
                    request.content()
            );
        } catch (RetryableException ex) {
            LOGGER.warn(JbstConstants.Logs.SERVER_OFFLINE, "GitHub", ex.getMessage());
            throw new IllegalArgumentException(ex);
        }
    }
}
