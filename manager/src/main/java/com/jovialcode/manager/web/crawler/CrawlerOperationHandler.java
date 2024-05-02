package com.jovialcode.manager.web.crawler;

import com.jovialcode.manager.web.ResponseMessage;
import lombok.NonNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public class CrawlerOperationHandler {
    public Mono<ServerResponse> run(@NonNull ServerRequest request){
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(ResponseMessage.of("hello")));
    }

    public Mono<ServerResponse> stop(@NonNull ServerRequest request){
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(ResponseMessage.of("hello")));
    }
}
