package jbst.foundation.feigns.clients.telegram.definitions;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface TelegramDefinition {
    @RequestLine("POST /bot{token}/sendMessage")
    @Headers("Content-Type: " + MediaType.APPLICATION_JSON_VALUE)
    void sendMessage(
            @Param("token") String token,
            @RequestBody Map<String, Object> body
    );
}
