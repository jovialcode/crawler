package com.jovialcode.manager.web.crawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class CrawlerRouter {
    @Autowired
    private CrawlerDescriptionHandler crawlerDescriptionHandler;

    @Autowired
    private CrawlerOperationHandler crawlerOperationHandler;

    @Bean
    public RouterFunction<ServerResponse> descriptionRoute(){
        return RouterFunctions
            .route(GET("/crawler").and(accept(MediaType.APPLICATION_JSON)), crawlerDescriptionHandler::crawler)
            .andRoute(GET("/crawlers").and(accept(MediaType.APPLICATION_JSON)), crawlerDescriptionHandler::crawlers)
            .andRoute(POST("/createCrawler").and(accept(MediaType.APPLICATION_JSON)), crawlerDescriptionHandler::createCrawler)
            .andRoute(PUT("/modifyCrawler").and(accept(MediaType.APPLICATION_JSON)), crawlerDescriptionHandler::modifyCrawler)
            .andRoute(DELETE("/deleteCrawler").and(accept(MediaType.APPLICATION_JSON)), crawlerDescriptionHandler::deleteCrawler);
    }

    @Bean
    public RouterFunction<ServerResponse> actionRoute(){
        return RouterFunctions
            .route(GET("/run").and(accept(MediaType.APPLICATION_JSON)), crawlerOperationHandler::run)
            .andRoute(GET("/stop").and(accept(MediaType.APPLICATION_JSON)), crawlerOperationHandler::stop);
    }
}
