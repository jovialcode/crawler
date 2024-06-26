package com.jovialcode.manager.web.parsing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class ParsingRouter {

    @Autowired
    ParsingInfoTableHandler parsingInfoTableHandler;

    @Bean
    public RouterFunction<ServerResponse> tableRoute(){
        return RouterFunctions
            .route(GET("/parsing/table").and(accept(MediaType.APPLICATION_JSON)), parsingInfoTableHandler::table);
    }

}
