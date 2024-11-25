package jbst.foundation.feigns.github.domain.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// JSON
@JsonIgnoreProperties(ignoreUnknown = true)
public record GithubRepoContentsResponse(
        @JsonProperty("download_url") String downloadUrl
) {
}
