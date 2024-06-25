package io.tech1.framework.foundation.feigns.clients.github.domain.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// JSON
@JsonIgnoreProperties(ignoreUnknown = true)
public record GithubRepoContentsResponse(
        @JsonProperty("download_url") String downloadUrl
) {
}
