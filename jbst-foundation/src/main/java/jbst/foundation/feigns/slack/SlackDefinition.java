package jbst.foundation.feigns.slack;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface SlackDefinition {
    @RequestLine("POST /chat.postMessage")
    @Headers(
            {
                    "Authorization: Bearer {token}",
                    "Content-Type: " + MediaType.APPLICATION_JSON_VALUE
            }
    )
    void sendMessage(
            @Param("token") String token,
            @RequestBody Map<String, Object> requestBody
    );
}
