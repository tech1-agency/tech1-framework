package io.tech1.framework.domain.constants;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class SwaggerConstants {
    public static final List<String> ENDPOINTS = List.of(
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**"
    );
}
