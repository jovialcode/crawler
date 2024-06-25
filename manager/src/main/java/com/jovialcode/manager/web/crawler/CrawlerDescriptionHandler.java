package com.jovialcode.manager.web.crawler;

import com.jovialcode.manager.domains.crawler.CrawlerService;
import com.jovialcode.manager.web.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CrawlerDescriptionHandler {

    private final CrawlerService crawlerService;
    private final CrawlerMapper crawlerMapper;


    public Mono<ServerResponse> crawler(ServerRequest request){
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(ResponseMessage.of("hello")));
    }

    public Mono<ServerResponse> crawlers(ServerRequest request){
        Flux<CrawlerDescription> crawlerDescriptions = crawlerService.crawlers().map(crawlerMapper::mapToDTO);
        return ServerResponse.ok().body(crawlerDescriptions, CrawlerDescription.class);
    }


    public Mono<ServerResponse> createCrawler(ServerRequest request){
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ResponseMessage.of("hello")));
    }

    public Mono<ServerResponse> modifyCrawler(ServerRequest request){
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(ResponseMessage.of("hello")));
    }

    public Mono<ServerResponse> deleteCrawler(ServerRequest request){
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(ResponseMessage.of("hello")));
    }
}
