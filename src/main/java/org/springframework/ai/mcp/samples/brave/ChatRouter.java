package org.springframework.ai.mcp.samples.brave;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


import static org.springframework.web.reactive.function.server.RequestPredicates.POST;


@Configuration
public class ChatRouter {

    @Bean
    public RouterFunction<ServerResponse> route(ChatHandler chathandler) {
        return RouterFunctions.route(POST("api/chat"), chathandler::askQuestion);
    }



}
