package com.jovialcode.manager.infra;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class CrawlerClient {
    private final WebClient client;

    public CrawlerClient(WebClient.Builder builder) {
        this.client = builder
            .baseUrl("http://localhost:8080")
            .build();
    }
}
