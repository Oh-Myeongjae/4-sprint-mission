package com.sprint.mission.discodeit.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    servers = {
        @Server(
            url = "http://localhost:8080",
            description = "로컬 서버"
        )
    }
)
public class OpenApiConfig {

}
