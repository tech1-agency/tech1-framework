package jbst.foundation.feigns.slack;

import feign.RetryableException;
import jbst.foundation.domain.constants.JbstConstants;
import jbst.foundation.feigns.slack.domain.requests.SlackMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SlackClient {

    // Definitions
    private final SlackDefinition definition;

    public final void sendMessage(SlackMessageRequest request) {
        try {
            this.definition.sendMessage(
                    request.token(),
                    request.getRequestBody()
            );
        } catch (RetryableException ex) {
            LOGGER.warn(JbstConstants.Logs.SERVER_OFFLINE, "Slack", ex.getMessage());
            throw new IllegalArgumentException(ex);
        }
    }
}
