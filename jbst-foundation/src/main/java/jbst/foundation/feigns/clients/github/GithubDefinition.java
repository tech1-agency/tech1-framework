package jbst.foundation.feigns.clients.github;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import jbst.foundation.feigns.clients.github.domain.responses.GithubRepoContentsResponse;
import org.springframework.http.MediaType;

public interface GithubDefinition {
    @RequestLine("GET /repos/{owner}/{repo}/contents/{path}")
    @Headers(
            {
                    "Authorization: token {token}",
                    "Content-Type: " + MediaType.APPLICATION_JSON_VALUE
            }
    )
    GithubRepoContentsResponse getContents(
            @Param("token") String token,
            @Param("owner") String owner,
            @Param("repo") String repo,
            @Param("path") String path
    );
}
